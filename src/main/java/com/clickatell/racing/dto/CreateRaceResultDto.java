package com.clickatell.racing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;

@Data
public class CreateRaceResultDto {
    @NotNull(message = "Please enter the race Id")
    private Long raceId;
    @NotNull(message = "Please enter the rider Id")
    private Long riderId;
    @NotNull(message = "Please enter the finish time")
    private Duration finishTime;
}
