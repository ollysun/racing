package com.clickatell.racing.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getWeather(String location) {
        String apiKey = "8dad3db309e50de33c8cdefbe69cec74";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }
}
