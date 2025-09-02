package com.web.pharma.filter;

import com.web.pharma.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationWebFilter extends AuthenticationWebFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationWebFilter.class);

    public JwtAuthenticationWebFilter() {
        super(new JwtReactiveAuthenticationManager());
        setServerAuthenticationConverter(new JwtServerAuthenticationConverter());
        log.info("Initialized JwtAuthenticationWebFilter with custom authentication manager and converter");
    }

    static class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

        private static final Logger log = LoggerFactory.getLogger(JwtServerAuthenticationConverter.class);

        @Override
        public Mono<Authentication> convert(ServerWebExchange exchange) {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.debug("No valid Authorization header found in request");
                return Mono.empty();
            }

            String token = authHeader.substring(7);
            log.debug("Extracted JWT token from Authorization header");

            try {
                Map<String, Object> claims = JwtUtil.validateToken(token);
                String subject = (String) claims.get("sub");

                var authorities = JwtUtil.getRoles(claims).stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList());

                log.info("JWT validated successfully for subject: {}", subject);
                return Mono.just(new UsernamePasswordAuthenticationToken(subject, token, authorities));
            } catch (Exception e) {
                log.warn("Failed to validate JWT token: {}", e.getMessage());
                log.debug("JWT validation error details", e);
                return Mono.empty();
            }
        }
    }

    static class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

        private static final Logger log = LoggerFactory.getLogger(JwtReactiveAuthenticationManager.class);

        @Override
        public Mono<Authentication> authenticate(Authentication authentication) {
            log.debug("Authenticating already validated JWT for principal: {}", authentication.getPrincipal());
            return Mono.just(authentication);
        }
    }
}
