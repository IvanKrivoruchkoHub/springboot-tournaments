package com.example.springboottournaments.repository;

import com.example.springboottournaments.entity.Match;
import com.example.springboottournaments.entity.Tournament;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Optional<Tournament> findTournamentByMatchesContains(Match match);
}
