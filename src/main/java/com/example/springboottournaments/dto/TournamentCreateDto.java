package com.example.springboottournaments.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class TournamentCreateDto {
    private List<String> participantNicks;
    private Integer maxNumberParticipants;
}
