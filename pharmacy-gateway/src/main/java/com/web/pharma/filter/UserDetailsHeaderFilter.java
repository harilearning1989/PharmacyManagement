package com.web.pharma.filter;

import com.web.pharma.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
public class UserDetailsHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .switchIfEmpty(Mono.empty())   // ✅ no nulls, just empty
                .flatMap(auth -> {
                    if (auth == null) {
                        return chain.filter(exchange);
                    }

                    ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate()
                            .header("X-User-Id", auth.getName());

                    if (auth.getCredentials() instanceof String token) {
                        try {
                            var claims = JwtUtil.validateToken(token);

                            Optional.ofNullable(claims.get("email"))
                                    .ifPresent(email -> requestBuilder.header("X-User-Email", email.toString()));

                            Optional.ofNullable(claims.get("phone"))
                                    .ifPresent(phone -> requestBuilder.header("X-User-Phone", phone.toString()));

                            List<String> roles = JwtUtil.getRoles(claims);
                            if (!roles.isEmpty()) {
                                requestBuilder.header("X-User-Roles", String.join(",", roles));
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    return chain.filter(exchange.mutate().request(requestBuilder.build()).build());
                })
                .switchIfEmpty(chain.filter(exchange)); // ✅ no auth context, just continue
    }


    @Override
    public int getOrder() {
        return 0;
    }
}

