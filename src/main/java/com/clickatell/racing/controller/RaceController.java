package com.clickatell.racing.controller;

import com.clickatell.racing.dto.CreateRaceDto;
import com.clickatell.racing.dto.RaceResponseDto;
import com.clickatell.racing.entity.Race;
import com.clickatell.racing.service.RaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/races")
@RequiredArgsConstructor
public class RaceController {
    private final RaceService raceService;

    @PostMapping
    public ResponseEntity<RaceResponseDto> createRace(@RequestBody CreateRaceDto race) {
        return ResponseEntity.ok(raceService.createRace(race));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaceResponseDto> getRaceById(@PathVariable Long id) {
        return ResponseEntity.ok(raceService.getRaceById(id));
    }

    @GetMapping
    public ResponseEntity<List<RaceResponseDto>> getAllRaces() {
        return ResponseEntity.ok(raceService.getAllRaces());
    }

}
