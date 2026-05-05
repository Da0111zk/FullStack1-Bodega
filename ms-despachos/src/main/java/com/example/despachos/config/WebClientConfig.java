package com.example.despachos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ms.productos.url}")
    private String productosUrl;

    @Value("${ms.stock.url}")
    private String stockUrl;

    @Bean
    public WebClient webClientProductos(WebClient.Builder builder) {
        return builder.baseUrl(productosUrl).build();
    }

    @Bean
    public WebClient webClientStock(WebClient.Builder builder) {
        return builder.baseUrl(stockUrl).build();
    }
}