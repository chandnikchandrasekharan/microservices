package com.shop.cartinventory.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String PRODUCT_SERVICE_URL = "http://localhost:8082/api/products";

    @Qualifier("productService")
    @Bean
    @LoadBalanced
    public WebClient getWebClientBuilderForProduct() {
        return WebClient.builder()
                .baseUrl(PRODUCT_SERVICE_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
