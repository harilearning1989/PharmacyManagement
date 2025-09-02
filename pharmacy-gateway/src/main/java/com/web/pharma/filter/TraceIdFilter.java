package com.web.pharma.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
public class TraceIdFilter implements WebFilter {

    private static final String TRACE_ID = "traceId";
    private static final String TRACE_HEADER = "X-Trace-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 1️⃣ Read incoming traceId (if present) or generate new one
        final String headerTraceId = exchange.getRequest().getHeaders().getFirst(TRACE_HEADER);
        final String traceId = (headerTraceId != null) ? headerTraceId : UUID.randomUUID().toString();

        // 2️⃣ Add traceId to MDC (so Logback can use %X{traceId})
        MDC.put(TRACE_ID, traceId);
        log.info("Pharmacy Gateway TraceIdFilter applied, traceId={}", traceId);


        // 3️⃣ Mutate the request so downstream services also get traceId
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> builder.headers(h -> h.set(TRACE_HEADER, traceId)))
                .build();

        // 4️⃣ Continue filter chain, clean MDC afterwards
        return chain.filter(mutatedExchange)
                .doFinally(signal -> MDC.remove(TRACE_ID));
    }
}


