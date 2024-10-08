package com.clickatell.racing.repository;

import com.clickatell.racing.entity.Race;
import com.clickatell.racing.entity.RaceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceResultRepository extends JpaRepository<RaceResult, Long> {
    List<RaceResult> findTop3ByRaceAndDidNotFinishFalseOrderByFinishTimeAsc(Race race);
    List<RaceResult> findByRaceAndDidNotFinishTrue(Race race);
    @Query("SELECT rr FROM RaceResult rr WHERE rr.race.id = :raceId")
    List<RaceResult> findRaceResultsByRaceId(@Param("raceId") Long raceId);
}
