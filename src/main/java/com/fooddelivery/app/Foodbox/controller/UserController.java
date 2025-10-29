// UserController.java
package com.fooddelivery.app.Foodbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import com.fooddelivery.app.Foodbox.model.UserCust;
import com.fooddelivery.app.Foodbox.repository.UserCustRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    @Autowired
    private UserCustRepository UserCustRepository;
    
    @Autowired
    private JavaMailSender javaMailSender;



    @PostMapping("/registerCustomer")
    public Response registerCustomer(@RequestBody UserCust UserCust) {
        try {
        	
        	  if (UserCustRepository.existsByEmail(UserCust.getEmail())) {
                  return new Response("failed", "User already exists with this email!");
              }

              // Set role and save
        	  UserCust.setRole("CUSTOMER");
              UserCustRepository.save(UserCust);
              SimpleMailMessage message = new SimpleMailMessage();
              message.setTo(UserCust.getEmail());
              message.setSubject("Welcome to Amazon Portal – Registration Successful");

              StringBuilder emailContent = new StringBuilder();
              emailContent.append("Hi ").append(UserCust.getUsername()).append(",\n\n");
              emailContent.append("🎉 Welcome aboard! Your registration to the Amazon-like E-Commerce portal was successful.\n\n");
              emailContent.append("Here are your login credentials:\n");
              emailContent.append("📧 Email: ").append(UserCust.getEmail()).append("\n");
              emailContent.append("🔐 Password: ").append(UserCust.getPassword()).append("\n\n");
              emailContent.append("You can now log in and start exploring our products, adding items to your cart, and placing orders!\n\n");
              emailContent.append("If you have any questions, feel free to reply to this email or contact our support team.\n\n");
              emailContent.append("Thank you,\n");
              emailContent.append("Amazon Portal Team");

              message.setText(emailContent.toString());
              javaMailSender.send(message);


              return new Response("success", "Customer registered successfully!");
              
        } catch (Exception e) {
            return new Response("error", "Registration failed. Please try again.");
        }
    }
}