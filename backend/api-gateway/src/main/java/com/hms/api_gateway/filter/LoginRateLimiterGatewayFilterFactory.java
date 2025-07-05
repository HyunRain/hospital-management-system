package com.hms.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Component
public class LoginRateLimiterGatewayFilterFactory extends AbstractGatewayFilterFactory<LoginRateLimiterGatewayFilterFactory.Config> {

    private final RedisTemplate<String, String> redisTemplate;
    private static final int LIMIT = 5;
    private static final Duration REPLENISH_WINDOW = Duration.ofMinutes(30);


    public LoginRateLimiterGatewayFilterFactory(RedisTemplate<String, String> redisTemplate) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String key = "login:rate:" + getClientIp(exchange);
            return chain.filter(exchange).then(Mono.defer(() -> {
                HttpStatusCode statusCode = exchange.getResponse().getStatusCode();
                if (statusCode == HttpStatus.BAD_REQUEST) {
                    redisTemplate.opsForValue().increment(key);
                    redisTemplate.expire(key, REPLENISH_WINDOW);
                }

                String value = redisTemplate.opsForValue().get(key);
                int attempts = value != null ? Integer.parseInt(value) : 0;

                if (attempts > LIMIT) {
                    exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                    return exchange.getResponse().setComplete();
                }

                return Mono.empty();
            }));
        };
    }

    public static class Config {
        // You can add config fields here later if needed
    }

    private String getClientIp(ServerWebExchange exchange) {
        return Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
    }

}

