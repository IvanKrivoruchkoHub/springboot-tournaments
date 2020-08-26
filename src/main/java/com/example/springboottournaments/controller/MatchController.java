package com.example.springboottournaments.controller;

import com.example.springboottournaments.dto.MatchResultDto;
import com.example.springboottournaments.entity.Match;
import com.example.springboottournaments.entity.Tournament;
import com.example.springboottournaments.service.MatchService;
import com.example.springboottournaments.service.TournamentService;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/matches/")
public class MatchController {
    @Autowired
    private MatchService matchService;
    @Autowired
    private TournamentService tournamentService;

    @PutMapping("/{matchId}")
    public Match updateMatch(@PathVariable Long matchId,
                             @RequestBody MatchResultDto matchResultDto) {
        Match match = matchService.getById(matchId);
        Tournament tournament = tournamentService.getTournamentByMatchesContains(match);
        if (tournament.getPaused()) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "You can't update this match because tournament suspended");
        }
        if (match.getSecondParticipant() == null) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "You can't update this match. Second participant is absent");
        }

        tournament.incrementEliminationMatchesCount();
        tournamentService.save(tournament);
        match.setFinishTime(matchResultDto.getFinishTime());
        match.setFirstParticipantScore(matchResultDto.getFirstParticipantScore());
        match.setSecondParticipantScore(matchResultDto.getSecondParticipantScore());
        match = matchService.save(match);
        return match;
    }

    @PutMapping("/{matchId}/startTime")
    public Match updateStartTimeOfMatch(@PathVariable Long matchId,
                             @RequestParam
                             @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING) LocalTime localTime) {
        Match match = matchService.getById(matchId);
        match.setStartTime(localTime);
        return matchService.save(match);
    }

    @GetMapping("/{matchId}")
    public Match getMatchById(@PathVariable Long matchId) {
        return matchService.getById(matchId);
    }
}
