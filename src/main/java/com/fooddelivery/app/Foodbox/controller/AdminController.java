package com.fooddelivery.app.Foodbox.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fooddelivery.app.Foodbox.model.Role;
import com.fooddelivery.app.Foodbox.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.app.Foodbox.model.Item;
import com.fooddelivery.app.Foodbox.repository.ItemRepository;
import com.fooddelivery.app.Foodbox.repository.UserRepository;
import com.fooddelivery.app.Foodbox.service.FileStorageService;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	 @Autowired
	    private JavaMailSender javaMailSender;


    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public AdminController(UserRepository userRepository,
                         ItemRepository itemRepository,
                         FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/createUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createHotelUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("role") Role role,
            @RequestParam(value = "items", required = false) String itemsJson,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Create and save user
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setRole(role);
            User savedUser = userRepository.save(user);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Welcome to Amazon Portal – Registration Successful");

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Hi ").append(username).append(",\n\n");
            emailContent.append("🎉 Welcome aboard! Your registration to the Amazon-like E-Commerce portal was successful.\n\n");
            emailContent.append("Here are your login credentials:\n");
            emailContent.append("📧 Email: ").append(email).append("\n");
            emailContent.append("🔐 Password: ").append(password).append("\n\n");
            emailContent.append("You can now log in and start exploring our products, adding items to your cart, and placing orders!\n\n");
            emailContent.append("If you have any questions, feel free to reply to this email or contact our support team.\n\n");
            emailContent.append("Thank you,\n");
            emailContent.append("Amazon Portal Team");

            message.setText(emailContent.toString());
            javaMailSender.send(message);

            // 2. Process items if hotel
            List<Item> savedItems = new ArrayList<>();
            if (role == Role.HOTEL && itemsJson != null && images != null) {
                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, String>> items = mapper.readValue(itemsJson, 
                    new TypeReference<List<Map<String, String>>>() {});

                if (items.size() != images.length) {
                    throw new IllegalArgumentException("Number of items and images must match");
                }

                for (int i = 0; i < items.size(); i++) {
                    Map<String, String> itemData = items.get(i);
                    MultipartFile imageFile = images[i];

                    // Convert category from string to enum
                    Item.FoodCategory category = Item.FoodCategory.fromString(itemData.get("category"));

                    // Store the image
                    String storedFilename = fileStorageService.storeFileWithOriginalName(imageFile, username);

                    // Create and save item
                    Item item = new Item();
                    item.setName(itemData.get("name"));
                    item.setPrice(new BigDecimal(itemData.get("price")));
                    item.setCategory(category);
                    item.setImageName(storedFilename);
                    item.setImagePath("/images/" + username + "/" + storedFilename);
                    item.setHotel(savedUser);
                    
                    savedItems.add(itemRepository.save(item));
                }
            }

            // 3. Prepare response
            response.put("status", "success");
            response.put("message", "User created successfully");
            response.put("data", Map.of(
                "username", username,
                "items", savedItems.stream().map(item -> Map.of(
                    "name", item.getName(),
                    "price", item.getPrice(),
                    "category", item.getCategory().getDisplayName(),
                    "imageName", item.getImageName(),
                    "imagePath", item.getImagePath()
                )).collect(Collectors.toList())
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}