package com.shop.productservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.productservice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom queries if needed
}
