package com.fooddelivery.app.Foodbox.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.List;

import jakarta.persistence.*; 


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)  // Add this annotation
    private String username;
    private String password;
    private String email;
    

    @Enumerated(EnumType.STRING)
    private Role role;

    private String address; // Only for customers

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Item> items; // Only applicable if role is HOTEL

    // Constructors
    public User() {}

    public User(String username, String password, Role role, String address) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
        
    }

    // Getters and Setters
    
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
 
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
}

