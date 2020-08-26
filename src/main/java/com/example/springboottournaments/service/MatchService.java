package com.example.springboottournaments.service;

import com.example.springboottournaments.entity.Match;
import com.example.springboottournaments.entity.Participant;
import java.util.List;

public interface MatchService {
    List<Match> createAndSaveMatches(List<Participant> participants);
    List<Match> saveAll(List<Match> matches);
    Match getById(Long id);
    Match save(Match match);
    List<Participant> getWinnersOfMatches(List<Match> matches);
}
