package com.fooddelivery.app.Foodbox.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;


@Entity
@Table(name = "item")
public class Item {

    public enum FoodCategory {
        VEG("Veg"),
        NON_VEG("NonVeg"),
        DRINKS_AND_JUICES("DrinksAndJuices");

        private final String displayName;

        FoodCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static FoodCategory fromString(String text) {
            for (FoodCategory category : FoodCategory.values()) {
                if (category.displayName.equalsIgnoreCase(text)) {
                    return category;
                }
            }
            throw new IllegalArgumentException("No constant with text " + text + " found");
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(name = "original_image_name")
    private String originalImageName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodCategory category;  // Changed back to enum type

    private String imageName;
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "hotel_username", referencedColumnName = "username")
    private User hotel;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public String getOriginalImageName() {
        return originalImageName;
    }

    public void setOriginalImageName(String originalImageName) {
        this.originalImageName = originalImageName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public User getHotel() {
        return hotel;
    }

    public void setHotel(User hotel) {
        this.hotel = hotel;
    }
}