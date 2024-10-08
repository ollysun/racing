package com.clickatell.racing.service;

import com.clickatell.racing.dto.CreateRiderDto;
import com.clickatell.racing.dto.RaceResponseDto;
import com.clickatell.racing.dto.RiderResponseDto;
import com.clickatell.racing.entity.Race;
import com.clickatell.racing.entity.RaceResult;
import com.clickatell.racing.entity.Rider;
import com.clickatell.racing.exceptionhandling.ResourceException;
import com.clickatell.racing.exceptionhandling.ResourceNotFoundException;
import com.clickatell.racing.repository.RaceRepository;
import com.clickatell.racing.repository.RaceResultRepository;
import com.clickatell.racing.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderService {
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;
    private final RaceResultRepository raceResultRepository;


    public RiderResponseDto createRider(CreateRiderDto createRiderDto) {

        if (createRiderDto == null) {
            throw new ResourceException("create rider cannot be null");
        }
        Rider rider = modelMapper.map(createRiderDto, Rider.class);

        return modelMapper.map(riderRepository.save(rider), RiderResponseDto.class);
    }

    public RiderResponseDto getRiderById(Long id) {
        return riderRepository.findById(id)
                .map(rider -> modelMapper.map(rider, RiderResponseDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("Rider not found with id: " + id));
    }

    public List<RiderResponseDto> getAllRiders() {
        return riderRepository.findAll()
                .stream()
                .map(rider ->  modelMapper.map(rider, RiderResponseDto.class))
                .toList();
    }
}
