package com.example.springboottournaments.service;

import com.example.springboottournaments.entity.Match;
import com.example.springboottournaments.entity.Tournament;
import java.util.List;
import java.util.Map;

public interface TournamentService {
    Tournament save(Tournament tournament);
    Tournament getById(Long id);
    Tournament getTournamentByMatchesContains(Match match);
    Map<String, List<Match>> getGridByTournamentId(Long tournamentId);
    Tournament addParticipantToTournamentWithId(Long tournamentId,
                                                String participantNickName);
    void deleteParticipantFromTournament(Long tournamentId,
                                               String participantNickName);
}
