package com.hms.api_gateway.controller;

import com.hms.api_gateway.clients.BillingClient;
import com.hms.api_gateway.clients.PatientClient;
import com.hms.api_gateway.dto.BillingItemResponseDto;
import com.hms.api_gateway.dto.PaginatedBillingItemResponseDto;
import com.hms.api_gateway.dto.PatientDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Tag(name = "Gateway", description = "Aggregated API Gateway")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class GatewayController {
    private final BillingClient billingClient;
    private final PatientClient patientClient;

    public GatewayController(BillingClient billingClient, PatientClient patientClient) {
        this.billingClient = billingClient;
        this.patientClient = patientClient;
    }

    @GetMapping("/billings-with-patients")
    public Mono<PaginatedBillingItemResponseDto> getBillingsWithPatientNames(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        return billingClient.getBillings(page, size) // returns Mono<PaginatedBillingItemResponseDto>
                .doOnNext(System.out::println)
                .flatMap(billingItems -> {
                    List<String> patientIds = billingItems.getBillingItemResponseDtos()
                            .stream()
                            .map(BillingItemResponseDto::getPatientId)
                            .toList();

                    System.out.println(patientIds);

                    return patientClient.getPatientNames(patientIds) // Mono<List<PatientDto>>
                            .map(patientNames -> {
                                Map<String, String> patientMap = patientNames.stream()
                                        .collect(Collectors.toMap(PatientDto::getPatientId,
                                                p -> p.getFirstName() + " " + p.getLastName()));

                                billingItems.getBillingItemResponseDtos()
                                        .forEach(item -> item.setPatientName(patientMap.get(item.getPatientId())));

                                return billingItems;
                            });
                });
    }
}
