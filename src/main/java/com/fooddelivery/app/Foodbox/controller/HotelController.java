package com.fooddelivery.app.Foodbox.controller;

import com.fooddelivery.app.Foodbox.model.Item;
import com.fooddelivery.app.Foodbox.model.Role;
import com.fooddelivery.app.Foodbox.model.User;
import com.fooddelivery.app.Foodbox.repository.ItemRepository;
import com.fooddelivery.app.Foodbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    
    @Autowired
    public HotelController(UserRepository userRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * Get all hotels with complete information
     * @return List of HotelDTO objects containing id and name
     */
    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        List<User> hotels = userRepository.findByRole(Role.HOTEL);
        
        List<HotelDTO> hotelDTOs = hotels.stream()
            .map(hotel -> new HotelDTO(
                hotel.getUsername(), 
                hotel.getUsername() != null ? hotel.getUsername() : hotel.getUsername()))
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(hotelDTOs);
    }

    /**
     * Get only hotel names (simplified version)
     * @return List of hotel names as strings
     */
    @GetMapping("/names")
    public ResponseEntity<List<String>> getHotelNames() {
        List<String> hotelNames = userRepository.findByRole(Role.HOTEL)
            .stream()
            .map(user -> user.getUsername() != null ? user.getUsername() : user.getUsername())
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(hotelNames);
    }

    /**
     * Get menu for a specific hotel
     * @param hotelUsername The username of the hotel
     * @return List of menu items as MenuItemDTO
     */
    @GetMapping("/{hotelUsername}/menu")
    public ResponseEntity<List<MenuItemDTO>> getHotelMenu(
            @PathVariable String hotelUsername) {
        
        List<Item> items = itemRepository.findByHotelUsername(hotelUsername);
        
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        List<MenuItemDTO> menuItemDTOs = items.stream()
            .map(item -> new MenuItemDTO(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getCategory().name(),
                item.getImagePath()
            ))
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(menuItemDTOs);
    }

    // DTO Classes
    public static class HotelDTO {
        private final String id;
        private final String name;

        public HotelDTO(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() { return id; }
        public String getName() { return name; }
    }

    public static class MenuItemDTO {
        private final Long id;
        private final String name;
        private final BigDecimal price;
        private final String category;
        private final String imagePath;

        public MenuItemDTO(Long id, String name, BigDecimal price, 
                          String category, String imagePath) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.category = category;
            this.imagePath = imagePath;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public BigDecimal getPrice() { return price; }
        public String getCategory() { return category; }
        public String getImagePath() { return imagePath; }
    }
}