package com.clickatell.racing.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class RaceResultResponseDto {
    private Long id;
    private RaceResponseDto race;
    private RiderResponseDto rider;
    private Duration finishTime;
    private boolean didNotFinish;
}
