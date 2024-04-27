package com.shop.cartinventory.controller;

import com.shop.cartinventory.entity.CartItem;
import com.shop.cartinventory.entity.Product;
import com.shop.cartinventory.repository.CartItemRepository;


import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@RestController
@RequestMapping("/api/cartInventory")
public class CartItemController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private RestTemplate restTemplate; // Used for making HTTP requests to the Product Service

    private static final String PRODUCT_SERVICE_URL = "http://localhost:8082/api/products/";

    @GetMapping
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    @GetMapping("/{id}")
    public CartItem getCartItemById(@PathVariable Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));
    }
    
    
    @PostMapping
    public CartItem createCartItem(@Valid @RequestBody CartItem cartItem) {
        // Call Product Service to fetch product information
        Product product = restTemplate.getForObject(PRODUCT_SERVICE_URL + cartItem.getProductId(), Product.class);
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + cartItem.getProductId());
        }
        // Check if enough quantity is available
        if (product.getQuantity() < cartItem.getQuantity()) {
            throw new RuntimeException("Not enough quantity available for product: " + product.getName());
        }
        // Update product quantity
        product.setQuantity(product.getQuantity() - cartItem.getQuantity());
        restTemplate.put(PRODUCT_SERVICE_URL + cartItem.getProductId(), product); // Update product quantity
        // Save cart item to database
        return cartItemRepository.save(cartItem);
    }


    @PutMapping("/{id}")
    public CartItem updateCartItem(@PathVariable Long id, @Valid @RequestBody CartItem cartItemDetails) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));

        // Call Product Service to fetch product information
        Product product = restTemplate.getForObject(PRODUCT_SERVICE_URL + cartItemDetails.getProductId(), Product.class);
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + cartItemDetails.getProductId());
        }

        // Check if enough quantity is available
        if (product.getQuantity() < cartItemDetails.getQuantity()) {
            throw new RuntimeException("Not enough quantity available for product: " + product.getName());
        }

        // Update product quantity
        product.setQuantity(product.getQuantity() - cartItemDetails.getQuantity());
        restTemplate.put(PRODUCT_SERVICE_URL + cartItemDetails.getProductId(), product); // Update product quantity

        // Update cart item details
        cartItem.setProductId(cartItemDetails.getProductId());
        cartItem.setQuantity(cartItemDetails.getQuantity());
        // Update other attributes if needed

        return cartItemRepository.save(cartItem);
    }

    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartItemRepository.deleteById(id);
    }
}
