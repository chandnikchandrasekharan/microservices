package com.shop.cartinventory.controller;

import com.shop.cartinventory.entity.CartItem;
import com.shop.cartinventory.service.CartService;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cartInventory")
public class CartItemController {

    private final CartService cartService;

    public CartItemController(CartService cartService) {
        this.cartService = cartService;
    }


    @GetMapping
    public List<CartItem> getAllCartItems() {
        return cartService.getAllItem();
    }

    @GetMapping("/{id}")
    public CartItem getCartItemById(@PathVariable @NonNull Long id) {
        return cartService.getItemById(id);
    }


    @PostMapping
    public CartItem createCartItem(@Valid @NonNull @RequestBody CartItem cartItem) {
        return cartService.addToCart(cartItem);
    }


    @PutMapping("/{id}")
    public CartItem updateCartItem(@PathVariable Long id, @Valid @RequestBody CartItem cartItemDetails) {
        return cartService.updateCart(id,cartItemDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartService.removeFromCart(id);
    }
}
