package com.clickatell.racing.service;

import com.clickatell.racing.dto.CreateRiderDto;
import com.clickatell.racing.dto.RaceResponseDto;
import com.clickatell.racing.dto.RiderResponseDto;
import com.clickatell.racing.entity.Race;
import com.clickatell.racing.entity.RaceResult;
import com.clickatell.racing.entity.Rider;
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
            throw new RuntimeException("create rider cannot be null");
        }
        Rider rider = modelMapper.map(createRiderDto, Rider.class);

        return modelMapper.map(riderRepository.save(rider), RiderResponseDto.class);
    }

    public RiderResponseDto getRiderById(Long id) {
        return riderRepository.findById(id)
                .map(rider -> modelMapper.map(rider, RiderResponseDto.class))
                .orElseThrow(() -> new RuntimeException("Rider not found with id: " + id));
    }

    public List<RiderResponseDto> getAllRiders() {
        return riderRepository.findAll()
                .stream()
                .map(rider ->  modelMapper.map(rider, RiderResponseDto.class))
                .toList();
    }

//    public List<RiderResponseDto> getRidersWhoDidNotParticipate(Long raceId) {
//        // Get all race results for the specified race
//        List<RaceResult> raceResults = raceResultRepository.findRaceResultsByRaceId(raceId);
//
//        // Get the list of rider IDs that participated in the race
//        List<Long> participatedRiderIds = raceResults.stream()
//                .map(rr -> rr.getRider().getId())
//                .toList();
//
//        // Fetch all riders
//        List<Rider> allRiders = riderRepository.findAll();
//
//        // Filter riders who did not participate in the race
//        return allRiders.stream()
//                .filter(rider -> !participatedRiderIds.contains(rider.getId()))
//                .map(rider ->  modelMapper.map(rider, RiderResponseDto.class))
//                .toList();
//    }
}
