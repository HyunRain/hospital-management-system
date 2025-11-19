package com.hms.appointment_service.clients;

import com.hms.appointment_service.dto.DoctorDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class StaffClient {
    private final WebClient webClient;

    public StaffClient(@Value("${services.staff.url}") String staffServiceUrl) {
        this.webClient = WebClient.create(staffServiceUrl);
        System.out.println();
    }

    public Mono<List<DoctorDto>> getDoctorNames(List<String> ids) {
        return webClient.post()
                .uri("/api/staff/doctor/batch")
                .bodyValue(ids)
                .retrieve()
                .bodyToFlux(DoctorDto.class)
                .collectList();
    }
}
