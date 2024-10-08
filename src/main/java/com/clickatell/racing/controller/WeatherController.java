package com.clickatell.racing.controller;

import com.clickatell.racing.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/{location}")
    public ResponseEntity<String> getWeather(@PathVariable String location) {
        return ResponseEntity.ok(weatherService.getWeather(location));
    }
}
