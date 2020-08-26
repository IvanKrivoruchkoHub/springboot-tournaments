package com.example.springboottournaments.entity;

import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Participant firstParticipant;
    @ManyToOne
    private Participant secondParticipant;
    @Column(columnDefinition = "TIME")
    private LocalTime startTime;
    @Column(columnDefinition = "TIME")
    private LocalTime finishTime;
    private Integer firstParticipantScore;
    private Integer secondParticipantScore;
}
