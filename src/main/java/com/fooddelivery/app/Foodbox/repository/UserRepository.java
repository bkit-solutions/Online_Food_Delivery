package com.fooddelivery.app.Foodbox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fooddelivery.app.Foodbox.model.Role;
import com.fooddelivery.app.Foodbox.model.User;

//repository/UserRepository.java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
 User findByUsernameAndPassword(String username, String password);
 List<User> findByRole(Role role);

}


