package com.example.springboottournaments.controller;

import com.example.springboottournaments.dto.TournamentCreateDto;
import com.example.springboottournaments.entity.Match;
import com.example.springboottournaments.entity.Participant;
import com.example.springboottournaments.entity.Tournament;
import com.example.springboottournaments.service.ParticipantService;
import com.example.springboottournaments.service.TournamentService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/tournaments")
public class TournamentController {
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private ParticipantService participantService;

    @PostMapping
    public Tournament createTournament(@RequestBody TournamentCreateDto tournamentCreateDto) {
        if (tournamentCreateDto.getParticipantNicks().size()
            > tournamentCreateDto.getMaxNumberParticipants()) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "count of participants nickNames more than max number of participants");
        }
        List<Participant> participants = participantService.createParticipants(tournamentCreateDto.getParticipantNicks());
        Tournament tournament = new Tournament();
        tournament.setMaxNumberParticipants(tournamentCreateDto.getMaxNumberParticipants());
        tournament.setParticipants(participantService.saveAll(participants));
        return tournamentService.save(tournament);
    }

    @GetMapping("/{tournamentId}")
    public Tournament getTournament(@PathVariable Long tournamentId) {
        return tournamentService.getById(tournamentId);
    }

    @PutMapping("/{tournamentId}/pause")
    public Tournament pauseTournament(@PathVariable Long tournamentId,
                                      @RequestBody Boolean status) {
        Tournament tournament = tournamentService.getById(tournamentId);
        tournament.setPaused(status);
        return tournamentService.save(tournament);
    }

    @GetMapping("/{tournamentId}/grid")
    public Map<String, List<Match>> getGrid(@PathVariable Long tournamentId) {
        return tournamentService.getGridByTournamentId(tournamentId);
    }

    @PostMapping("/{tournamentId}/participants/")
    public Tournament addParticipantToTournament(@RequestBody String participantNickName,
                                           @PathVariable Long tournamentId) {
        return tournamentService.addParticipantToTournamentWithId(tournamentId, participantNickName);
    }

    @DeleteMapping("/{tournamentId}/participants/")
    public void deleteParticipantFromTournament(@RequestBody String participantNickName,
                                                 @PathVariable Long tournamentId) {
        tournamentService.deleteParticipantFromTournament(tournamentId, participantNickName);
    }
}
