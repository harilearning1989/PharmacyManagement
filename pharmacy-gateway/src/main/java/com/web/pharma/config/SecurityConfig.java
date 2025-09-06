package com.web.pharma.config;

import com.web.pharma.filter.JwtAuthenticationWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(withDefaults())  // ✅ enable CORS support
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/medicines/**").permitAll()
                        .pathMatchers("/supplier/**").permitAll()
                        //.pathMatchers("/medicines/**").hasRole("ADMIN")
                        .pathMatchers("/admin/**").hasRole("ADMIN")
                        .pathMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .anyExchange().authenticated()
                )
                .addFilterAt(new JwtAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}

