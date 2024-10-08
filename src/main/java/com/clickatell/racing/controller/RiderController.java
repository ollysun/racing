package com.clickatell.racing.controller;

import com.clickatell.racing.dto.CreateRiderDto;
import com.clickatell.racing.dto.RiderResponseDto;
import com.clickatell.racing.service.RiderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
public class RiderController {
    private final RiderService riderService;

    @PostMapping
    public ResponseEntity<RiderResponseDto> createRider(@Valid @RequestBody CreateRiderDto rider) {
        return ResponseEntity.ok(riderService.createRider(rider));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RiderResponseDto> getRiderById(@PathVariable Long id) {
        return ResponseEntity.ok(riderService.getRiderById(id));
    }

    @GetMapping
    public ResponseEntity<List<RiderResponseDto>> getAllRiders() {
        return ResponseEntity.ok(riderService.getAllRiders());
    }


}
