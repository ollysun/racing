package com.clickatell.racing.controllers;

import com.clickatell.racing.controller.RaceController;
import com.clickatell.racing.dto.CreateRaceDto;
import com.clickatell.racing.dto.RaceResponseDto;
import com.clickatell.racing.service.RaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(RaceController.class)
class RaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RaceService raceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateRace() throws Exception {
        // Mocking the service response
        RaceResponseDto responseDto = new RaceResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Mountain Race");
        responseDto.setLocation("Hills");
        responseDto.setStartTime(LocalDateTime.now());

        // Mock service behavior
        when(raceService.createRace(any(CreateRaceDto.class))).thenReturn(responseDto);

        // CreateRaceDto input object
        CreateRaceDto createRaceDto = new CreateRaceDto();
        createRaceDto.setName("Mountain Race");
        createRaceDto.setLocation("Hills");
        createRaceDto.setStartTime(LocalDateTime.now());

        // Perform post request and verify
        mockMvc.perform(post("/races")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRaceDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Mountain Race"))
                .andExpect(jsonPath("$.location").value("Hills"));
    }

    @Test
    void testGetRaceById() throws Exception {
        // Mocking the service response
        RaceResponseDto responseDto = new RaceResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Mountain Race");
        responseDto.setLocation("Hills");
        responseDto.setStartTime(LocalDateTime.now());

        // Mock service behavior
        when(raceService.getRaceById(anyLong())).thenReturn(responseDto);

        // Perform get request and verify
        mockMvc.perform(MockMvcRequestBuilders.get("/races/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Mountain Race"))
                .andExpect(jsonPath("$.location").value("Hills"));
    }

    @Test
    void testGetAllRaces() throws Exception {
        // Mocking the service response
        RaceResponseDto race1 = new RaceResponseDto();
        race1.setId(1L);
        race1.setName("Race One");
        race1.setLocation("Hills");
        race1.setStartTime(LocalDateTime.now());

        RaceResponseDto race2 = new RaceResponseDto();
        race2.setId(2L);
        race2.setName("Race Two");
        race2.setLocation("Mountains");
        race2.setStartTime(LocalDateTime.now());

        List<RaceResponseDto> raceList = Arrays.asList(race1, race2);

        // Mock service behavior
        when(raceService.getAllRaces()).thenReturn(raceList);

        // Perform get request and verify
        mockMvc.perform(get("/races"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Race One"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Race Two"));
    }
}
