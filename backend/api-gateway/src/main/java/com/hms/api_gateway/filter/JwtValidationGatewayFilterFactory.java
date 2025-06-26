package com.hms.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
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
            ServerHttpRequest request = exchange.getRequest();
            HttpMethod method = request.getMethod();

            if ("OPTIONS".equalsIgnoreCase(String.valueOf(method))) {
                exchange.getResponse().setStatusCode(HttpStatus.OK);
                // You may also want to add CORS headers here explicitly if needed
                return exchange.getResponse().setComplete();
            }

            List<HttpCookie> cookies = request.getCookies().get("token");
            String token = null;

            if (cookies != null && !cookies.isEmpty()) {
                token = "Bearer " + cookies.getFirst().getValue();
            }

            // If the token is still null or does not start with "Bearer", return unauthorized
            if(token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Admin role check for specific paths
            if (path.equals("/api/auth/registration") || path.equals("/api/staff") || path.equals("/api/staff/all") || path.equals("/api/staff/delete/{email}")) {
                return webClient.get()
                        .uri("/api/auth/extractRole")
                        .header(HttpHeaders.AUTHORIZATION, token)
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
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .toBodilessEntity()
                    .then(chain.filter(exchange));
        };
    }

}
