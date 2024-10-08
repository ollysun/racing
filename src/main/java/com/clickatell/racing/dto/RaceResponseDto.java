package com.clickatell.racing.dto;


import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class RaceResponseDto {
    private Long id;
    private String name;
    private LocalDateTime startTime;
    private String location;
    private Duration raceDuration;
}
