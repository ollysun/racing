package com.clickatell.racing.repository;

import com.clickatell.racing.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {

    @Query("SELECT r FROM Rider r WHERE r.id NOT IN :riderIds")
    List<Rider> findByIdNotIn(@Param("riderIds") List<Long> riderIds); //to find riders who did not take part in a race.
}

