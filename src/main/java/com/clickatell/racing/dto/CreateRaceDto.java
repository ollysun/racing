package com.clickatell.racing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class CreateRaceDto {
    private Long id;
    @NotBlank(message = "please enter the name")
    private String name;
    @NotNull(message = "Please enter the start time")
    private LocalDateTime startTime;
    @NotBlank(message = "please enter the location")
    private String location;
    @NotNull(message = "please enter the race Duration")
    private Duration raceDuration;
}
