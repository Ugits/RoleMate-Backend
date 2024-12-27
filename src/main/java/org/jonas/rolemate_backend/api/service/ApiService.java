package org.jonas.rolemate_backend.api.service;

import org.jonas.rolemate_backend.api.dto.ErrorResponseDTO;
import org.jonas.rolemate_backend.api.dto.SpellDTO;
import org.jonas.rolemate_backend.exception.WebServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ApiService {

    private final WebClient webClient;

    @Autowired
    public ApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<SpellDTO>> fetchSpellsByLevel(Integer level) {
        return webClient.get()
                .uri("/usable/{level}", level)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(ErrorResponseDTO.class)
                                .flatMap(externalError -> {
                                    OffsetDateTime timestamp = parseTimestamp(externalError.timestamp());
                                    return Mono.error(new WebServiceException(
                                            externalError.statusCode(),
                                            externalError.message(),
                                            timestamp
                                    ));
                                })
                )
                .bodyToFlux(SpellDTO.class)
                .collectList()
                .onErrorResume(throwable -> {
                    if (throwable instanceof WebServiceException) {
                        return Mono.error(throwable);
                    } else if (throwable instanceof WebClientRequestException) {
                        return Mono.error(new WebServiceException(
                                HttpStatus.BAD_GATEWAY.value(),
                                "Unable to connect to the external service.",
                                OffsetDateTime.now()
                        ));
                    } else {
                        return Mono.error(new WebServiceException(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "An unexpected error occurred.",
                                OffsetDateTime.now()
                        ));
                    }
                });
    }

    private OffsetDateTime parseTimestamp(String timestamp) {
        try {
            return OffsetDateTime.parse(timestamp);
        } catch (Exception e) {
            return OffsetDateTime.now();
        }
    }



}




