package com.ubbcluj.tutorial.controller;

import com.ubbcluj.tutorial.dto.DeadlineReminderDto;
import com.ubbcluj.tutorial.dto.DeadlineReminderResponseDto;
import com.ubbcluj.tutorial.service.DeadlineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeadlineController {

    private final DeadlineService deadlineService;

    public DeadlineController(final DeadlineService deadlineService) {
        this.deadlineService = deadlineService;
    }

    @PostMapping("/compute-deadline")
    public ResponseEntity<DeadlineReminderResponseDto> computeDeadline(@RequestBody DeadlineReminderDto deadlineReminderDto) {
        return ResponseEntity.ok(this.deadlineService.getDeadlineReminder(deadlineReminderDto));
    }
}
