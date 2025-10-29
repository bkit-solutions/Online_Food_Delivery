package com.fooddelivery.app.Foodbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

import com.fooddelivery.app.Foodbox.model.User;
import com.fooddelivery.app.Foodbox.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username,
                                     @RequestParam String password) {
        User user = userService.authenticate(username, password);
        Map<String, Object> response = new HashMap<>();

        if (user == null) {
            response.put("status", "fail");
            response.put("message", "Invalid credentials");
        } else {
            response.put("status", "success");
            response.put("role", user.getRole().toString());
        }
        return response;
    }
}
