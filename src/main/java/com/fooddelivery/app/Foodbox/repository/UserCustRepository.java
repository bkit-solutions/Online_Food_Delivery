package com.fooddelivery.app.Foodbox.repository;


import com.fooddelivery.app.Foodbox.model.UserCust;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCustRepository extends JpaRepository<UserCust, Long> {
	UserCust findByUsername(String username);
	  boolean existsByEmail(String email);
}
