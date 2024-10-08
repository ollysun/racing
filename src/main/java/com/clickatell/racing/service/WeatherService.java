package com.clickatell.racing.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${weather-key}")
    private String apiKey;

    public String getWeather(String location) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }
}
