package com.clickatell.racing.services;

import com.clickatell.racing.dto.CreateRaceDto;
import com.clickatell.racing.dto.RaceResponseDto;
import com.clickatell.racing.entity.Race;
import com.clickatell.racing.repository.RaceRepository;
import com.clickatell.racing.service.RaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RaceServiceTest {

    @Mock
    private RaceRepository raceRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RaceService raceService;

    private CreateRaceDto createRaceDto;
    private Race race;
    private RaceResponseDto raceResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample test data setup
        createRaceDto = new CreateRaceDto();
        createRaceDto.setName("Mountain Bike Race");
        createRaceDto.setLocation("Mountain Trails");
        createRaceDto.setStartTime(LocalDateTime.now());

        race = new Race();
        race.setId(1L);
        race.setName("Mountain Bike Race");
        race.setLocation("Mountain Trails");
        race.setStartTime(LocalDateTime.now());

        raceResponseDto = new RaceResponseDto();
        raceResponseDto.setId(1L);
        raceResponseDto.setName("Mountain Bike Race");
        raceResponseDto.setLocation("Mountain Trails");
        raceResponseDto.setStartTime(LocalDateTime.now());
    }

    @Test
    void testCreateRace() {
        // Mock modelMapper behavior
        when(modelMapper.map(createRaceDto, Race.class)).thenReturn(race);
        when(modelMapper.map(race, RaceResponseDto.class)).thenReturn(raceResponseDto);
        when(raceRepository.save(any(Race.class))).thenReturn(race);

        RaceResponseDto savedRace = raceService.createRace(createRaceDto);
        assertNotNull(savedRace);
        assertEquals("Mountain Bike Race", savedRace.getName());

        verify(raceRepository, times(1)).save(any(Race.class));
        verify(modelMapper, times(1)).map(createRaceDto, Race.class);
        verify(modelMapper, times(1)).map(race, RaceResponseDto.class);
    }

    @Test
    void testCreateRace_NullInput() {
        // Test for null input
        RuntimeException exception = assertThrows(RuntimeException.class, () -> raceService.createRace(null));
        assertEquals("create race cannot be null", exception.getMessage());
    }

    @Test
    void testGetRaceById_Success() {
        // Mock repository and modelMapper behavior
        when(raceRepository.findById(1L)).thenReturn(Optional.of(race));
        when(modelMapper.map(race, RaceResponseDto.class)).thenReturn(raceResponseDto);

        RaceResponseDto foundRace = raceService.getRaceById(1L);
        assertNotNull(foundRace);
        assertEquals(1L, foundRace.getId());
        assertEquals("Mountain Bike Race", foundRace.getName());

        verify(raceRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(race, RaceResponseDto.class);
    }

    @Test
    void testGetRaceById_NotFound() {
        // Mock repository for race not found
        when(raceRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> raceService.getRaceById(1L));
        assertEquals("Race not found with id: 1", exception.getMessage());

        verify(raceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllRaces() {
        // Mock repository and modelMapper behavior for multiple races
        Race race2 = new Race();
        race2.setId(2L);
        race2.setName("Desert Bike Race");
        race2.setLocation("Desert Trails");
        race2.setStartTime(LocalDateTime.now());
        RaceResponseDto raceResponseDto2 = new RaceResponseDto();
        raceResponseDto2.setId(2L);
        raceResponseDto2.setName("Desert Bike Race");
        raceResponseDto2.setLocation("Desert Trails");
        raceResponseDto2.setStartTime(LocalDateTime.now());

        List<Race> races = Arrays.asList(race, race2);
        List<RaceResponseDto> expectedRaces = Arrays.asList(raceResponseDto, raceResponseDto2);

        when(raceRepository.findAll()).thenReturn(races);
        when(modelMapper.map(race, RaceResponseDto.class)).thenReturn(raceResponseDto);
        when(modelMapper.map(race2, RaceResponseDto.class)).thenReturn(raceResponseDto2);

        List<RaceResponseDto> actualRaces = raceService.getAllRaces();
        assertNotNull(actualRaces);
        assertEquals(2, actualRaces.size());

        verify(raceRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(race, RaceResponseDto.class);
        verify(modelMapper, times(1)).map(race2, RaceResponseDto.class);
    }
}
