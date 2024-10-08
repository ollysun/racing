package com.clickatell.racing.service;

import com.clickatell.racing.dto.CreateRaceDto;
import com.clickatell.racing.dto.RaceResponseDto;
import com.clickatell.racing.entity.Race;
import com.clickatell.racing.exceptionhandling.ResourceException;
import com.clickatell.racing.exceptionhandling.ResourceNotFoundException;
import com.clickatell.racing.repository.RaceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RaceService {
    private final RaceRepository raceRepository;
    private final ModelMapper modelMapper;

    public RaceResponseDto createRace(CreateRaceDto createRaceDto) {
        if (createRaceDto == null) {
            throw new ResourceException("create race cannot be null");
        }
        Race race = modelMapper.map(createRaceDto, Race.class);
        return modelMapper.map(raceRepository.save(race), RaceResponseDto.class);
    }

    public RaceResponseDto getRaceById(Long id) {
        return raceRepository.findById(id)
                .map(race -> modelMapper.map(race, RaceResponseDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("Race not found with id: " + id));
    }

    public List<RaceResponseDto> getAllRaces() {
        return raceRepository.findAll()
                .stream()
                .map(race ->  modelMapper.map(race, RaceResponseDto.class))
                .toList();

    }

}
