package com.example.springboottournaments.repository;

import com.example.springboottournaments.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
