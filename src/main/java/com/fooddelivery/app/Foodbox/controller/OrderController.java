package com.fooddelivery.app.Foodbox.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fooddelivery.app.Foodbox.model.Order;
import com.fooddelivery.app.Foodbox.model.OrderItem;
import com.fooddelivery.app.Foodbox.model.Role;
import com.fooddelivery.app.Foodbox.model.User;
import com.fooddelivery.app.Foodbox.repository.OrderRepository;
import com.fooddelivery.app.Foodbox.repository.UserRepository;

// A DTO to receive the custom structure from frontend
class OrderRequest {
	public String deliveryAddress;
	public String hotel;
	public List<CartItem> cart;
}

class CartItem {
	public String name;
	public int price;
	public int quantity;
}

@RestController
@RequestMapping("/api")
///@CrossOrigin(origins = "*")
public class OrderController {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/submit")
	public ResponseEntity<?> submitOrder(@RequestBody OrderRequest request) {

		System.out.println("Hotel: " + request.hotel);
		System.out.println("Address: " + request.deliveryAddress);
		System.out.println("Items: " + request.cart.size());

		// Step 1: Create Order entity
		Order order = new Order();
		order.setDeliveryAddress(request.deliveryAddress);
		order.setHotel(request.hotel); // assuming setHotel exists
		order.setStatus("PLACED"); // 🟡 Set initial status to PLACED (Pending Order)

		// Step 2: Convert cart items to OrderItem list
		List<OrderItem> orderItems = request.cart.stream().map(cartItem -> {
			OrderItem item = new OrderItem();
			item.setFoodName(cartItem.name);
			item.setPrice(cartItem.price);
			item.setQuantity(cartItem.quantity);
			item.setOrder(order); // associate item with this order
			return item;
		}).collect(Collectors.toList());

		// Step 3: Attach items to the order
		order.setOrderItems(orderItems);

		// Step 4: Save the order (cascade saves orderItems)
		orderRepository.save(order);

		return ResponseEntity.ok("Order submitted successfully to " + request.hotel);
	}

	@GetMapping
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	/*@GetMapping("/hotels")
	public List<String> getAllHotelNames() {
		return userRepository.findByRole(Role.HOTEL).stream().map(User::getUsername).collect(Collectors.toList());
	}*/

	@GetMapping("/orders/hotel/{username}")
	public List<Order> getOrdersByHotelUsername(@PathVariable String username) {
		return orderRepository.findByHotel(username);
	}
//    @GetMapping("/orders")
//    public List<Order> getOrdersByHotel(@RequestParam String hotel) {
//        return orderRepository.findByHotel(hotel);
//    }

	 //@PostMapping("/{orderId}/ready")
	  
	  @PostMapping("/orders/{orderId}/ready") public ResponseEntity<String>
	  markOrderAsReady(@PathVariable Long orderId) { Optional<Order> optionalOrder
	  = orderRepository.findById(orderId);
	  
	  if (optionalOrder.isEmpty()) { return
	  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found"); }
	  
	  Order order = optionalOrder.get();
	  
	  if (!"PLACED".equalsIgnoreCase(order.getStatus())) { return
	  ResponseEntity.status(HttpStatus.BAD_REQUEST).
	  body("Only placed orders can be marked as ready"); }
	  
	  order.setStatus("READY"); orderRepository.save(order);
	  
	  return ResponseEntity.ok("Order marked as ready"); }
	 
	@PostMapping("/orders/{orderId}/completed")
	public ResponseEntity<?> markOrderAsCompleted(@PathVariable Long orderId) {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);

		if (!optionalOrder.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
		}

		Order order = optionalOrder.get();
		order.setStatus("COMPLETED"); // make sure 'status' field exists in your entity
		orderRepository.save(order);

		return ResponseEntity.ok("Order marked as completed");
	}

	// Method to fetch orders with 'READY' status
	@GetMapping("/orders/ready")
	public ResponseEntity<List<Order>> getReadyOrders() {
		List<Order> readyOrders = orderRepository.findByStatus("READY");
		return ResponseEntity.ok(readyOrders);
	}

	// Method to fetch orders with 'COMPLETED' status
	@GetMapping("/orders/completed")
	public ResponseEntity<List<Order>> getCompletedOrders() {
		List<Order> completedOrders = orderRepository.findByStatus("COMPLETED");
		return ResponseEntity.ok(completedOrders);
	}
}
