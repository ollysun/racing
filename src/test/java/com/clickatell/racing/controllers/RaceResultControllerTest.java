package com.clickatell.racing.controllers;

import com.clickatell.racing.controller.RaceResultController;
import com.clickatell.racing.dto.CreateRaceResultDto;
import com.clickatell.racing.dto.RaceResultResponseDto;
import com.clickatell.racing.dto.RiderResponseDto;
import com.clickatell.racing.service.RaceResultService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(RaceResultController.class)
class RaceResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RaceResultService raceResultService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateRaceResult() throws Exception {

        // Arrange
        CreateRaceResultDto createRaceResultDto = new CreateRaceResultDto();
        createRaceResultDto.setRaceId(1L);
        createRaceResultDto.setRiderId(1L);
        createRaceResultDto.setDidNotFinish(false);  // or true based on the test scenario
        createRaceResultDto.setFinishTime(Duration.ofMinutes(120));


        // Mocking the service response
        RaceResultResponseDto responseDto = new RaceResultResponseDto();
        responseDto.setId(1L);
        responseDto.setFinishTime(Duration.ofHours(1).plusMinutes(30)); // 1h 30m
        responseDto.setDidNotFinish(false);


        // Mock service behavior
        when(raceResultService.createRaceResult(any(CreateRaceResultDto.class))).thenReturn(responseDto);

        // Convert CreateRaceResultDto to JSON
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Register the module
        String createRaceResultDtoJson = objectMapper.writeValueAsString(createRaceResultDto);

        // Perform POST request and verify
        mockMvc.perform(post("/race-results")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRaceResultDtoJson))  // Add the JSON request body
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.finishTime").value("PT1H30M"))  // Format for Duration
                .andExpect(jsonPath("$.didNotFinish").value(false));

        // Verify the service was called once
        verify(raceResultService, times(1)).createRaceResult(any(CreateRaceResultDto.class));



    }

    @Test
    void testGetTop3Riders() throws Exception {
        // Mocking the service response
        RaceResultResponseDto rider1 = new RaceResultResponseDto();
        rider1.setId(1L);
        rider1.setFinishTime(Duration.ofHours(1).plusMinutes(30));

        RaceResultResponseDto rider2 = new RaceResultResponseDto();
        rider2.setId(2L);
        rider2.setFinishTime(Duration.ofHours(1).plusMinutes(35));

        List<RaceResultResponseDto> top3Riders = Arrays.asList(rider1, rider2);

        // Mock service behavior
        when(raceResultService.getTop3Riders(anyLong())).thenReturn(top3Riders);

        // Perform GET request and verify
        mockMvc.perform(get("/race-results/top3/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].finishTime").value("PT1H30M"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].finishTime").value("PT1H35M"));
    }

    @Test
    void testGetRidersWhoDidNotFinish() throws Exception {
        // Mocking the service response
        RaceResultResponseDto rider1 = new RaceResultResponseDto();
        rider1.setId(1L);
        rider1.setDidNotFinish(true);

        List<RaceResultResponseDto> dnfRiders = Arrays.asList(rider1);

        // Mock service behavior
        when(raceResultService.getRidersWhoDidNotFinish(anyLong())).thenReturn(dnfRiders);

        // Perform GET request and verify
        mockMvc.perform(get("/race-results/dnf/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].didNotFinish").value(true));
    }

    @Test
    void testGetRidersWhoDidNotParticipate() throws Exception {
        // Mocking the service response
        RiderResponseDto rider1 = new RiderResponseDto();
        rider1.setId(1L);
        rider1.setName("John Doe");

        RiderResponseDto rider2 = new RiderResponseDto();
        rider2.setId(2L);
        rider2.setName("Jane Smith");

        List<RiderResponseDto> nonParticipants = Arrays.asList(rider1, rider2);

        // Mock service behavior
        when(raceResultService.getRidersWhoDidNotParticipate(anyLong())).thenReturn(nonParticipants);

        // Perform GET request and verify
        mockMvc.perform(get("/race-results/not-participated/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));
    }
}
