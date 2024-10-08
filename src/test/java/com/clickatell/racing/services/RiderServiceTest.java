package com.clickatell.racing.services;

import com.clickatell.racing.dto.CreateRiderDto;
import com.clickatell.racing.dto.RiderResponseDto;
import com.clickatell.racing.entity.RaceResult;
import com.clickatell.racing.entity.Rider;
import com.clickatell.racing.repository.RaceResultRepository;
import com.clickatell.racing.repository.RiderRepository;
import com.clickatell.racing.service.RaceResultService;
import com.clickatell.racing.service.RiderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class RiderServiceTest {

    @Autowired
    private RiderService riderService;

    @MockBean
    private RiderRepository riderRepository;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private RaceResultRepository raceResultRepository;

    private CreateRiderDto createRiderDto;
    private Rider rider;
    private RiderResponseDto riderResponseDto;



    @BeforeEach
    void setUp() {
        // Initialize test data
        createRiderDto = new CreateRiderDto();
        createRiderDto.setName("John Doe");
        createRiderDto.setEmail("john.doe@example.com");

        rider = new Rider();
        rider.setId(1L);
        rider.setName("John Doe");
        rider.setEmail("john.doe@example.com");

        riderResponseDto = new RiderResponseDto();
        riderResponseDto.setId(1L);
        riderResponseDto.setName("John Doe");
        riderResponseDto.setEmail("john.doe@example.com");
    }

    @Test
    void testCreateRider() {
        // Mock modelMapper and repository behavior
        when(modelMapper.map(createRiderDto, Rider.class)).thenReturn(rider);
        when(riderRepository.save(any(Rider.class))).thenReturn(rider);
        when(modelMapper.map(rider, RiderResponseDto.class)).thenReturn(riderResponseDto);

        // Call service
        RiderResponseDto savedRider = riderService.createRider(createRiderDto);

        // Assert results
        assertNotNull(savedRider);
        assertEquals("John Doe", savedRider.getName());
        assertEquals("john.doe@example.com", savedRider.getEmail());

        // Verify that repository save was called once
        verify(riderRepository, times(1)).save(any(Rider.class));
        verify(modelMapper, times(1)).map(createRiderDto, Rider.class);
        verify(modelMapper, times(1)).map(rider, RiderResponseDto.class);
    }

    @Test
    void testCreateRider_NullInput() {
        // Test that a null CreateRiderDto input throws an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> riderService.createRider(null));
        assertEquals("create rider cannot be null", exception.getMessage());
    }

    @Test
    void testGetRiderById_Success() {
        // Mock repository and modelMapper behavior for finding rider by ID
        when(riderRepository.findById(1L)).thenReturn(Optional.of(rider));
        when(modelMapper.map(rider, RiderResponseDto.class)).thenReturn(riderResponseDto);

        // Call service
        RiderResponseDto foundRider = riderService.getRiderById(1L);

        // Assert results
        assertNotNull(foundRider);
        assertEquals(1L, foundRider.getId());
        assertEquals("John Doe", foundRider.getName());

        // Verify repository and mapper were called
        verify(riderRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(rider, RiderResponseDto.class);
    }

    @Test
    void testGetRiderById_NotFound() {
        // Mock repository behavior for rider not found
        when(riderRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert that the exception is thrown for non-existent rider
        RuntimeException exception = assertThrows(RuntimeException.class, () -> riderService.getRiderById(1L));
        assertEquals("Rider not found with id: 1", exception.getMessage());

        // Verify repository interaction
        verify(riderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllRiders() {
        // Mock repository and modelMapper behavior for multiple riders
        Rider rider2 = new Rider();
        rider2.setId(2L);
        rider2.setName("Jane Doe");
        rider2.setEmail("jane.doe@example.com");
        RiderResponseDto riderResponseDto2 = new RiderResponseDto();
        riderResponseDto2.setId(2L);
        riderResponseDto2.setName("Jane Doe");
        riderResponseDto2.setEmail("jane.doe@example.com");

        List<Rider> riders = Arrays.asList(rider, rider2);
        List<RiderResponseDto> expectedRiders = Arrays.asList(riderResponseDto, riderResponseDto2);

        when(riderRepository.findAll()).thenReturn(riders);
        when(modelMapper.map(rider, RiderResponseDto.class)).thenReturn(riderResponseDto);
        when(modelMapper.map(rider2, RiderResponseDto.class)).thenReturn(riderResponseDto2);

        // Call service
        List<RiderResponseDto> actualRiders = riderService.getAllRiders();

        // Assert results
        assertNotNull(actualRiders);
        assertEquals(2, actualRiders.size());

        // Verify repository and modelMapper were called
        verify(riderRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(rider, RiderResponseDto.class);
        verify(modelMapper, times(1)).map(rider2, RiderResponseDto.class);
    }



}
