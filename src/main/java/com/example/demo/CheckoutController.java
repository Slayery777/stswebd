package com.example.demo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class CheckoutController {
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/order")
    public String placeOrder(@RequestParam String name,
                            @RequestParam String address,
                            @RequestParam String cart,
                            Model model) {
        try {
            // Parse the cart items from the JSON string
            List<CartItem> cartItems = Arrays.asList(new ObjectMapper().readValue(cart, CartItem[].class));

            // Process each cart item
            for (CartItem item : cartItems) {
                Long productId = item.getProductId();
                int quantity = item.getQuantity();

                // Create a new order in the database
                Order order = new Order();
                order.setCustomerName(name);
                order.setCustomerAddress(address);
                order.setProductId(productId);
                order.setQuantity(quantity);
                order.setTotal(item.getProduct().getPrice().multiply(new BigDecimal(quantity)));
                System.out.println("Saving order: " + order);
                orderRepository.save(order);
            }

            // Display a confirmation message
            model.addAttribute("message", "Ваше замовлення було розміщено!");
            return "order-confirmation";
        } catch (JsonProcessingException e) {
            // Handle the exception
            model.addAttribute("error", "An error occurred while processing your order. Please try again.");
            return "checkout";
        }
    }
}
