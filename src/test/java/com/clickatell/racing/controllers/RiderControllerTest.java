package com.clickatell.racing.controllers;

import com.clickatell.racing.controller.RiderController;
import com.clickatell.racing.dto.CreateRiderDto;
import com.clickatell.racing.dto.RiderResponseDto;
import com.clickatell.racing.service.RiderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RiderController.class)
class RiderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RiderService riderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateRider() throws Exception {
        // Mocking the service response
        RiderResponseDto responseDto = new RiderResponseDto();
        responseDto.setId(1L);
        responseDto.setName("John Doe");
        responseDto.setEmail("john.doe@example.com");

        // Mock service behavior
        when(riderService.createRider(any(CreateRiderDto.class))).thenReturn(responseDto);

        // CreateRiderDto input object
        CreateRiderDto createRiderDto = new CreateRiderDto();
        createRiderDto.setName("John Doe");
        createRiderDto.setEmail("john.doe@example.com");

        // Perform post request and verify
        mockMvc.perform(post("/riders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRiderDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testGetRiderById() throws Exception {
        // Mocking the service response
        RiderResponseDto responseDto = new RiderResponseDto();
        responseDto.setId(1L);
        responseDto.setName("John Doe");
        responseDto.setEmail("john.doe@example.com");

        // Mock service behavior
        when(riderService.getRiderById(anyLong())).thenReturn(responseDto);

        // Perform get request and verify
        mockMvc.perform(get("/riders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testGetAllRiders() throws Exception {
        // Mocking the service response
        RiderResponseDto rider1 = new RiderResponseDto();
        rider1.setId(1L);
        rider1.setName("John Doe");
        rider1.setEmail("john.doe@example.com");

        RiderResponseDto rider2 = new RiderResponseDto();
        rider2.setId(2L);
        rider2.setName("Jane Smith");
        rider2.setEmail("jane.smith@example.com");

        List<RiderResponseDto> riderList = Arrays.asList(rider1, rider2);

        // Mock service behavior
        when(riderService.getAllRiders()).thenReturn(riderList);

        // Perform get request and verify
        mockMvc.perform(get("/riders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].email").value("jane.smith@example.com"));
    }
}
