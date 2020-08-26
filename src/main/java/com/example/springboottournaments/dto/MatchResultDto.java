package com.example.springboottournaments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class MatchResultDto {
    private Integer firstParticipantScore;
    private Integer secondParticipantScore;
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime finishTime;
}
