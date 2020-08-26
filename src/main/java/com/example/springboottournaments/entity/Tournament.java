package com.example.springboottournaments.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tournaments")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer maxNumberParticipants;
    @ManyToMany(cascade = CascadeType.ALL)
    List<Participant> participants = new ArrayList<>();
    @ManyToMany
    List<Match> matches = new ArrayList<>();
    private Integer eliminationMatchesCount = 0;
    private Boolean paused = false;

    public void incrementEliminationMatchesCount() {
        eliminationMatchesCount++;
    }

    public void addMatches(List<Match> matches) {
        this.matches.addAll(matches);
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public void deleteParticipantFromTournament(Participant participant) {
        participants.remove(participant);
    }

    public void deleteAllMatches(List<Match> matches) {
        this.matches.removeAll(matches);
    }
}
