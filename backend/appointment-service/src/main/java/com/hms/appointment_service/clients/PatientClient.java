package com.hms.appointment_service.clients;


import com.hms.appointment_service.dto.PatientDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class PatientClient {
    private final WebClient webClient;

    public PatientClient(@Value("${services.patient.url}") String patientServiceUrl) {
        this.webClient = WebClient.create(patientServiceUrl);
    }

    public Mono<List<PatientDto>> getPatientNames(List<String> ids) {
        return webClient.post()
                .uri("/api/patient/batch")
                .bodyValue(ids)
                .retrieve()
                .bodyToFlux(PatientDto.class)
                .collectList();
    }
}
