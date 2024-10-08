package com.clickatell.racing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class RaceResult {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        private Rider rider;

        @ManyToOne
        private Race race;

        private Duration finishTime;

        // for json output of duration as string
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        public Duration getFinishTime() {
                return finishTime;
        }

        private boolean didNotFinish;

}
