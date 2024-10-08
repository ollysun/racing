package com.clickatell.racing.service;

import com.clickatell.racing.dto.CreateRaceResultDto;
import com.clickatell.racing.dto.RaceResultResponseDto;
import com.clickatell.racing.dto.RiderResponseDto;
import com.clickatell.racing.entity.Race;
import com.clickatell.racing.entity.RaceResult;
import com.clickatell.racing.entity.Rider;
import com.clickatell.racing.repository.RaceResultRepository;
import com.clickatell.racing.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RaceResultService {
    private final RaceResultRepository raceResultRepository;
    private final RiderRepository riderRepository;
    private final RaceService raceService;
    private final RiderService riderService;
    private final ModelMapper modelMapper;

    public RaceResultResponseDto createRaceResult(CreateRaceResultDto createRaceResultDto) {
        RaceResult raceResult = new RaceResult();
        Race race = modelMapper.map(raceService.getRaceById(createRaceResultDto.getRaceId()), Race.class);
        Rider rider = modelMapper.map(riderService.getRiderById(createRaceResultDto.getRiderId()), Rider.class);
        raceResult.setRace(race);
        raceResult.setRider(rider);
        raceResult.setFinishTime(createRaceResultDto.getFinishTime());
        if(createRaceResultDto.getFinishTime().isZero()){
            raceResult.setDidNotFinish(true);
        }
        return modelMapper.map(raceResultRepository.save(raceResult), RaceResultResponseDto.class);
    }

    public List<RaceResultResponseDto> getTop3Riders(Long raceId) {
        Race race = modelMapper.map(raceService.getRaceById(raceId), Race.class);
        return raceResultRepository.findTop3ByRaceAndDidNotFinishFalseOrderByFinishTimeAsc(race)
                .stream()
                .map(raceResult ->  modelMapper.map(raceResult, RaceResultResponseDto.class))
                .toList();

    }

    public List<RaceResultResponseDto> getRidersWhoDidNotFinish(Long raceId) {
        Race race = modelMapper.map(raceService.getRaceById(raceId), Race.class);
        return raceResultRepository.findByRaceAndDidNotFinishTrue(race)
                .stream()
                .map(raceResult ->  modelMapper.map(raceResult, RaceResultResponseDto.class))
                .toList();
    }

    public List<RiderResponseDto> getRidersWhoDidNotParticipate(Long raceId) {
        // Get all race results for the specified race
        List<RaceResult> raceResults = raceResultRepository.findRaceResultsByRaceId(raceId);
        if(raceResults.isEmpty()) {
            return new ArrayList<>(); // If there is no information about the race return empty
        }
        // Get the list of rider IDs that participated in the race
        List<Long> participatedRiderIds = raceResults.stream()
                .map(rr -> rr.getRider().getId())
                .toList();

        // Fetch all riders
        List<Rider> allRiders = riderRepository.findAll();

        // Filter riders who did not participate in the race
        return allRiders.stream()
                .filter(rider -> !participatedRiderIds.contains(rider.getId()))
                .map(rider ->  modelMapper.map(rider, RiderResponseDto.class))
                .toList();
    }
}
