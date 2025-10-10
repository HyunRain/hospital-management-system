package com.hms.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.*;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private static final Logger log = LoggerFactory.getLogger(JwtValidationGatewayFilterFactory.class);
    private final WebClient webClient;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder, @Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().value();
            System.out.println(path);
            ServerHttpRequest request = exchange.getRequest();
            HttpMethod method = request.getMethod();

//            if ("OPTIONS".equalsIgnoreCase(String.valueOf(method))) {
//                exchange.getResponse().setStatusCode(HttpStatus.OK);
//                return exchange.getResponse().setComplete();
//            }

            if(path.equals("/api/auth/refresh") || path.equals("/api/auth/logout")) {
                log.info("Bypassing JWT validation for path: {}", path);
                // Bypass JWT validation for these paths
                return chain.filter(exchange);
            }

            List<HttpCookie> accessCookies = request.getCookies().get("accessToken");
            List<HttpCookie> refreshCookies = request.getCookies().get("refreshToken");
            String accessToken = null;
            String refreshToken = null;

            if (accessCookies != null && !accessCookies.isEmpty()) {
                accessToken = "Bearer " + accessCookies.getFirst().getValue();
            }

            if (refreshCookies != null && !refreshCookies.isEmpty()) {
                refreshToken = "Bearer " + refreshCookies.getFirst().getValue();
            }

            if (accessToken == null || !accessToken.startsWith("Bearer ")) {
                if (refreshToken != null) {
                    return respondUnauthorized(exchange, "Expired Access Token, but refresh token is available");
                }
                return respondUnauthorized(exchange, "Expired Access Token");
            }

            // Admin role check for specific paths
            if (isAdminRestrictedPath(path)) {
                return webClient.get()
                        .uri("/api/auth/extractRole")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .retrieve()
                        .bodyToMono(String.class)
                        .flatMap(role -> {
                            if (!"ADMIN".equals(role)) {
                                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                                return exchange.getResponse().setComplete();
                            }
                            return chain.filter(exchange);
                        });
            }

            return webClient.get()
                    .uri("/api/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            return chain.filter(exchange);
                        } else {
                            exchange.getResponse().setStatusCode(clientResponse.statusCode());
                            return clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                        byte[] bytes = errorBody.getBytes(StandardCharsets.UTF_8);
                                        return exchange.getResponse()
                                                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
                                    });
                        }
                    });

        };
    }

    private Mono<Void> respondUnauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(
                ("{\"error\": \"" + message + "\"}").getBytes(StandardCharsets.UTF_8)
        );
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private boolean isAdminRestrictedPath(String path) {
        return path.equals("/api/auth/registration") ||
                path.equals("/api/staff") ||
                path.equals("/api/staff/all") ||
                path.equals("/api/department/all") ||
                path.startsWith("/api/staff/delete/");
    }


}
