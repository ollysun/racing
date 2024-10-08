package com.clickatell.racing.services;

import com.clickatell.racing.dto.CreateRaceResultDto;
import com.clickatell.racing.dto.RaceResponseDto;
import com.clickatell.racing.dto.RaceResultResponseDto;
import com.clickatell.racing.dto.RiderResponseDto;
import com.clickatell.racing.entity.Race;
import com.clickatell.racing.entity.RaceResult;
import com.clickatell.racing.entity.Rider;
import com.clickatell.racing.repository.RaceResultRepository;
import com.clickatell.racing.repository.RiderRepository;
import com.clickatell.racing.service.RaceResultService;
import com.clickatell.racing.service.RaceService;
import com.clickatell.racing.service.RiderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class RaceResultServiceTest {

    @Autowired
    private RaceResultService raceResultService;

    @MockBean
    private RaceResultRepository raceResultRepository;

    @MockBean
    private RiderRepository riderRepository;

    @MockBean
    private RaceService raceService;

    @MockBean
    private RiderService riderService;

    @MockBean
    private ModelMapper modelMapper;

    private Rider rider1;
    private Rider rider2;
    private Race race;
    private RaceResult raceResult1;

    @BeforeEach
    void setUp() {
        // Initialize test data
        rider1 = new Rider();
        rider1.setId(1L);
        rider1.setName("John Doe");

        rider2 = new Rider();
        rider2.setId(2L);
        rider2.setName("Jane Smith");

        race = new Race();
        race.setId(1L);
        race.setName("Mountain Race");

        raceResult1 = new RaceResult();
        raceResult1.setId(1L);
        raceResult1.setRider(rider1);
        raceResult1.setRace(race);
        raceResult1.setFinishTime(Duration.ofHours(2));
        raceResult1.setDidNotFinish(true);  // Mark did not finish as true

    }

    @Test
    void testCreateRaceResult() {

        // Arrange
        CreateRaceResultDto createRaceResultDto = new CreateRaceResultDto();
        createRaceResultDto.setRaceId(1L);
        createRaceResultDto.setRiderId(1L);
        createRaceResultDto.setFinishTime(Duration.ofMinutes(120));

        // Mock behavior for race and rider services
        when(raceService.getRaceById(1L)).thenReturn(mockRaceResponseDto());
        when(riderService.getRiderById(1L)).thenReturn(mockRiderResponseDto());

        // Mock behavior for model mapper
        when(modelMapper.map(any(), eq(Race.class))).thenReturn(race);
        when(modelMapper.map(any(), eq(Rider.class))).thenReturn(rider1);
        when(modelMapper.map(any(RaceResult.class), eq(RaceResultResponseDto.class)))
                .thenReturn(mockRaceResultResponseDto());

        // Mock save operation in the repository
        when(raceResultRepository.save(any(RaceResult.class))).thenReturn(raceResult1);

        // Call the service method
        RaceResultResponseDto result = raceResultService.createRaceResult(createRaceResultDto);

        // Assert the result is not null
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(raceResultRepository, times(1)).save(any(RaceResult.class));

        // You can also verify additional properties of the RaceResult object
        verify(modelMapper, times(1)).map(any(RaceResult.class), eq(RaceResultResponseDto.class));
        verify(raceService, times(1)).getRaceById(1L);
        verify(riderService, times(1)).getRiderById(1L);
    }

    @Test
    void testGetTop3Riders() {
        // Mock raceService.getRaceById to return a RaceResponseDto object
        when(raceService.getRaceById(1L)).thenReturn(mockRaceResponseDto());

        // Ensure the ModelMapper maps RaceResponseDto to a Race entity
        when(modelMapper.map(any(RaceResponseDto.class), eq(Race.class))).thenReturn(race);

        // Mock repository return for findTop3ByRaceOrderByFinishTimeAsc
        when(raceResultRepository.findTop3ByRaceAndDidNotFinishFalseOrderByFinishTimeAsc(any(Race.class)))
                .thenReturn(Arrays.asList(raceResult1));

        // Mock the modelMapper conversion from RaceResult to RaceResultResponseDto
        when(modelMapper.map(any(RaceResult.class), eq(RaceResultResponseDto.class)))
                .thenReturn(mockRaceResultResponseDto());

        // Call the service method
        List<RaceResultResponseDto> top3Riders = raceResultService.getTop3Riders(1L);

        // Debug output to see what the test is returning
        System.out.println("Top 3 Riders Size: " + top3Riders.size());

        // Validate results
        assertNotNull(top3Riders);
        assertEquals(1, top3Riders.size());

        // Verify that the repository was called once
        verify(raceResultRepository, times(1)).findTop3ByRaceAndDidNotFinishFalseOrderByFinishTimeAsc(race);
    }

    @Test
    void testGetRidersWhoDidNotFinish() {
        // Mock raceService to return a RaceResponseDto
        when(raceService.getRaceById(1L)).thenReturn(mockRaceResponseDto());

        // Ensure the modelMapper maps the RaceResponseDto to a Race entity correctly
        when(modelMapper.map(any(RaceResponseDto.class), eq(Race.class))).thenReturn(race);

        // Mock repository return for findByRaceAndDidNotFinishTrue
        when(raceResultRepository.findByRaceAndDidNotFinishTrue(any(Race.class)))
                .thenReturn(Arrays.asList(raceResult1));  // Return a list containing raceResult1

        // Mock the modelMapper conversion from RaceResult to RaceResultResponseDto
        when(modelMapper.map(any(RaceResult.class), eq(RaceResultResponseDto.class)))
                .thenReturn(mockRaceResultResponseDto());

        // Call the service method
        List<RaceResultResponseDto> didNotFinishRiders = raceResultService.getRidersWhoDidNotFinish(1L);

        // Debug output to check what the mock repository is returning
        System.out.println("Riders who did not finish: " + didNotFinishRiders.size());

        // Verify the size of the list
        assertNotNull(didNotFinishRiders);
        assertEquals(1, didNotFinishRiders.size(), "Expected 1 rider who did not finish");

        // Verify that the repository method was called once
        verify(raceResultRepository, times(1)).findByRaceAndDidNotFinishTrue(race);
    }


    @Test
    void testGetRidersWhoDidNotParticipate() {
        when(raceResultRepository.findRaceResultsByRaceId(1L))
                .thenReturn(Arrays.asList(raceResult1));

        when(riderRepository.findAll()).thenReturn(Arrays.asList(rider1, rider2));

        when(modelMapper.map(any(Rider.class), eq(RiderResponseDto.class)))
                .thenReturn(mockRiderResponseDto());

        // Call the service method
        List<RiderResponseDto> nonParticipants = raceResultService.getRidersWhoDidNotParticipate(1L);

        assertNotNull(nonParticipants);
        assertEquals(1, nonParticipants.size());
        verify(raceResultRepository, times(1)).findRaceResultsByRaceId(1L);
        verify(riderRepository, times(1)).findAll();
    }

    private RaceResultResponseDto mockRaceResultResponseDto() {
        RaceResultResponseDto dto = new RaceResultResponseDto();
        dto.setId(1L);
        return dto;
    }

    private RiderResponseDto mockRiderResponseDto() {
        RiderResponseDto dto = new RiderResponseDto();
        dto.setId(1L);
        dto.setName("John Doe");
        return dto;
    }

    private RaceResponseDto mockRaceResponseDto() {
        RaceResponseDto dto = new RaceResponseDto();
        dto.setId(1L);
        dto.setName("Mountain Race");
        return dto;
    }
}
