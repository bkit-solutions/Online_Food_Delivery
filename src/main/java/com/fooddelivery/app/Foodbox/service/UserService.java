package com.fooddelivery.app.Foodbox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooddelivery.app.Foodbox.model.User;
import com.fooddelivery.app.Foodbox.repository.UserRepository;

//service/UserService.java
@Service
public class UserService {
 @Autowired
 private UserRepository repo;

 public User authenticate(String username, String password) {
     return repo.findByUsernameAndPassword(username, password);
 }
//Save user method
 public User saveUser(User user) {
     return repo.save(user);
 }
}
