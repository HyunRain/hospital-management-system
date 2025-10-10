package com.hms.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class GlobalErrorHandlingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandlingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String originalUri = exchange.getRequest().getURI().toString();

        return chain.filter(exchange)
                .onErrorResume(throwable -> {
                    log.error("Downstream service call failed while calling [{}]:", originalUri, throwable);
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                    response.getHeaders().setContentType(MediaType.TEXT_PLAIN);

                    String message = getString(originalUri);
                    byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
                });
    }

    private static String getString(String originalUri) {
        String message = "Service not available";
        if(originalUri.equals("http://localhost:8079/api/auth/login")) {
            message = "Login Service is currently unavailable.";
        }
        if(originalUri.startsWith("http://localhost:8079/api/patient")) {
            message = "Patient Service is currently unavailable.";
        }
        if(originalUri.startsWith("http://localhost:8079/api/staff")) {
            message = "Staff Service is currently unavailable.";
        }
        if(originalUri.startsWith("http://localhost:8081/api/billing")) {
            message = "Billing Service is currently unavailable.";
        }
        return message;
    }


    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
