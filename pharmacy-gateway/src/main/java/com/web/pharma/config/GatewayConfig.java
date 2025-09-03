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
                        .uri("lb://authenticate-service"))//8082
                .route("inventory-service", r -> r.path("/medicines/**")
                        .uri("lb://inventory-service"))//8083
                .route("customer-service", r -> r.path("/customer/**")
                        .uri("lb://customer-service"))//8084
                .route("payment-service", r -> r.path("/payment/**")
                        .uri("lb://payment-service"))
                .route("supplier-service", r -> r.path("/supplier/**")
                        .uri("lb://supplier-service"))
                .route("doctor-service", r -> r.path("/doctor/**")
                        .uri("lb://doctor-service"))
                .route("report-service", r -> r.path("/report/**")
                        .uri("lb://report-service"))
                .route("support-service", r -> r.path("/support/**")
                        .uri("lb://support-service"))
                .route("notification-service", r -> r.path("/notification/**")
                        .uri("lb://notification-service"))
                .build();
    }

}

