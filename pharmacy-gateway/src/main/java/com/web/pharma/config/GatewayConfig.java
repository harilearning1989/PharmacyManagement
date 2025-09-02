package com.web.pharma.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authenticate-service", r -> r.path("/auth/**")
                        .uri("http://localhost:8082"))
                .route("inventory-service", r -> r.path("/medicines/**")
                        .uri("http://localhost:8083"))
                .route("admin-service", r -> r.path("/admin/**")
                        .uri("http://localhost:9002"))
                .route("orders-composite", r -> r.path("/orders/**")
                        .uri("http://localhost:9001"))
                .build();
    }
}

