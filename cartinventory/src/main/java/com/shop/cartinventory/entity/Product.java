package com.shop.cartinventory.entity;

import lombok.Data;

@Data
public class Product {
   
    //private Long id;
    
    private Long productId;
    private String name;

    private double price;
    private int quantity;

    // Other attributes, constructors, getters, and setters
}
