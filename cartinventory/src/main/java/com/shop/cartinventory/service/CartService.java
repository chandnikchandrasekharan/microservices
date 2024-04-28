package com.shop.cartinventory.service;

import com.shop.cartinventory.entity.CartItem;
import com.shop.cartinventory.entity.Product;
import com.shop.cartinventory.exception.ClientException;
import com.shop.cartinventory.exception.NoRecordFoundException;
import com.shop.cartinventory.repository.CartItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class CartService {

    private final WebClient webClient;
    private final CartItemRepository cartItemRepository;

    public CartService(@Qualifier("productService") WebClient webClient, CartItemRepository cartItemRepository) {
        this.webClient = webClient;
        this.cartItemRepository = cartItemRepository;
    }


    public List<CartItem> getAllItem() {
        log.info("fetching all items");
        return cartItemRepository.findAll();
    }

    public CartItem getItemById(Long itemId) {
        log.info("fetching item by id :: {}", itemId);
        return cartItemRepository.findById(itemId)
                .orElseThrow(() -> new NoRecordFoundException("Item not found in cart with id: " + itemId));
    }

    public CartItem addToCart(CartItem cartItem) {

        Product product = webClient.get()
                .uri("/{productId}", cartItem.getProductId())
                .retrieve()
                .bodyToMono(Product.class).log()
                .blockOptional()
                .orElseThrow(() -> new NoRecordFoundException("No record found in Product service for id :: " + cartItem.getProductId()));


        if (product.getQuantity() < cartItem.getQuantity()) {
            throw new IllegalArgumentException("Insufficient quantity available for product: " + product.getQuantity());
        }

        product.setQuantity(product.getQuantity() - cartItem.getQuantity());

        webClient.put()
                .uri("/{productId}", cartItem.getProductId())
                .body(Mono.just(product), Product.class)
                .retrieve()
                .bodyToMono(Product.class).log()
                .onErrorMap(Exception.class, ex -> new ClientException(ex.getMessage()));

        return cartItemRepository.save(cartItem);
    }

    public CartItem updateCart(Long cartItemId, CartItem cartItemDetails) {
        CartItem cartItem = this.getItemById(cartItemId);

        Product product = webClient.get()
                .uri("/{productId}", cartItem.getProductId())
                .retrieve()
                .bodyToMono(Product.class).log()
                .blockOptional()
                .orElseThrow(() -> new NoRecordFoundException("No record found in Product service for id :: " + cartItem.getProductId()));


        if (product.getQuantity() < cartItem.getQuantity()) {
            throw new IllegalArgumentException("Insufficient quantity available for product: " + product.getQuantity());
        }

        product.setQuantity(product.getQuantity() - cartItem.getQuantity());

        webClient.put()
                .uri("/{productId}", cartItem.getProductId())
                .body(Mono.just(product), Product.class)
                .retrieve()
                .bodyToMono(Product.class).log()
                .onErrorMap(Exception.class, ex -> new ClientException(ex.getMessage()));

        cartItem.setProductId(cartItemDetails.getProductId());
        cartItem.setQuantity(cartItemDetails.getQuantity());

        return cartItemRepository.save(cartItem);
    }

    public void removeFromCart(Long id) {
        boolean isExists = cartItemRepository.existsById(id);

        if (isExists) {
            cartItemRepository.deleteById(id);
        } else {
            throw new NoRecordFoundException("Item not found in cart with id: " + id);
        }
    }
}
