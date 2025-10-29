package com.fooddelivery.app.Foodbox.repository;

import com.fooddelivery.app.Foodbox.model.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByHotel(String hotel);
	List<Order> findByStatus(String status);
}

