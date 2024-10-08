package com.clickatell.racing.controller;


import com.clickatell.racing.dto.CreateRaceResultDto;
import com.clickatell.racing.dto.RaceResultResponseDto;
import com.clickatell.racing.dto.RiderResponseDto;
import com.clickatell.racing.service.RaceResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/race-results")
@RequiredArgsConstructor
public class RaceResultController {

    private final RaceResultService raceResultService;

    @PostMapping
    public ResponseEntity<RaceResultResponseDto> createRaceResult(@RequestBody @Valid CreateRaceResultDto createRaceResultDto) {
        return ResponseEntity.ok(raceResultService.createRaceResult(createRaceResultDto));
    }

    @GetMapping("/top3/{raceId}")
    public ResponseEntity<List<RaceResultResponseDto>> getTop3Riders(@PathVariable Long raceId) {
        return ResponseEntity.ok(raceResultService.getTop3Riders(raceId));
    }

    @GetMapping("/dnf/{raceId}")
    public ResponseEntity<List<RaceResultResponseDto>> getRidersWhoDidNotFinish(@PathVariable Long raceId) {
        return ResponseEntity.ok(raceResultService.getRidersWhoDidNotFinish(raceId));
    }

    @GetMapping("/not-participated/{raceId}")
    public ResponseEntity<List<RiderResponseDto>> getRidersWhoDidNotParticipate(@PathVariable Long raceId) {
        List<RiderResponseDto> riders = raceResultService.getRidersWhoDidNotParticipate(raceId);
        return ResponseEntity.ok(riders);
    }
}
