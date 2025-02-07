package com.ubbcluj.tutorial.service;

import com.ubbcluj.tutorial.dto.DeadlineReminderDto;
import com.ubbcluj.tutorial.dto.DeadlineReminderResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DeadlineService {

    private final RestTemplate restTemplate;

    @Value("${lambdaURL}")
    private String lambdaUrl;

    public DeadlineService() {
        this.restTemplate  = new RestTemplate();
    }

    public DeadlineReminderResponseDto getDeadlineReminder(DeadlineReminderDto deadlineReminderDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entityLambda =
                new HttpEntity<>(deadlineReminderDto, headers);

        ResponseEntity<DeadlineReminderResponseDto> responseLambda =
                restTemplate.exchange(lambdaUrl, HttpMethod.POST, entityLambda, DeadlineReminderResponseDto.class);

        return responseLambda.getBody();
    }
}
