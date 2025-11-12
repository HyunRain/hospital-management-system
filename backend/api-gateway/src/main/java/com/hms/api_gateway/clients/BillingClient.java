package com.hms.api_gateway.clients;

import com.hms.api_gateway.dto.PaginatedBillingItemResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BillingClient {
    WebClient webClient;

    public BillingClient(@Value("${services.billing.url}") String billingServiceUrl) {
        this.webClient = WebClient.create(billingServiceUrl);
    }

    public Mono<PaginatedBillingItemResponseDto> getBillings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/billing/all")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(PaginatedBillingItemResponseDto.class);
    }
}
