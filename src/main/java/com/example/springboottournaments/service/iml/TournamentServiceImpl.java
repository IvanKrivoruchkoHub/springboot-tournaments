package com.example.springboottournaments.service.iml;

import com.example.springboottournaments.entity.Match;
import com.example.springboottournaments.entity.Participant;
import com.example.springboottournaments.entity.Tournament;
import com.example.springboottournaments.repository.TournamentRepository;
import com.example.springboottournaments.service.MatchService;
import com.example.springboottournaments.service.ParticipantService;
import com.example.springboottournaments.service.TournamentService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TournamentServiceImpl implements TournamentService {
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private MatchService matchService;
    @Autowired
    private ParticipantService participantService;


    private Map<String, List<Match>> getGrid(Tournament tournament) {
        Map<String, List<Match>> grid = new HashMap<>();
        int countOfMatches = tournament.getParticipants().size() / 2
            + tournament.getParticipants().size() % 2;
        for (int i = 0; i < tournament.getMatches().size();) {
            List<Match> tempMatches = tournament.getMatches()
                .stream()
                .skip(i)
                .limit(countOfMatches)
                .collect(Collectors.toList());
            i += countOfMatches;
            if (countOfMatches == 1) {
                grid.put("Final", tempMatches);
            } else {
                grid.put("1/"
                        + (countOfMatches % 2 == 0 ? countOfMatches : countOfMatches + 1),
                    tempMatches);
                countOfMatches = countOfMatches / 2 + countOfMatches % 2;
            }
        }
        return grid;
    }

    @Override
    public Tournament save(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament getById(Long id) {
        Optional<Tournament> optionalTournament = tournamentRepository.findById(id);
        return optionalTournament.get();
    }

    @Override
    public Tournament getTournamentByMatchesContains(Match match) {
        return tournamentRepository.findTournamentByMatchesContains(match).get();
    }

    @Override
    public Map<String, List<Match>> getGridByTournamentId(Long tournamentId) {
        Tournament tournament = getById(tournamentId);
        if (tournament.getMatches().size() == 0) {
            List<Participant> participants = tournament.getParticipants();
            Collections.shuffle(participants);
            List<Match> matches = matchService.createAndSaveMatches(participants);
            tournament.addMatches(matches);
            return getGrid(save(tournament));
        }
        Map<String, List<Match>> grid = getGrid(tournament);
        if (tournament.getMatches()
            .stream()
            .anyMatch(match -> match.getFirstParticipantScore() == null
                && match.getSecondParticipantScore() == null
                && match.getSecondParticipant() != null)) {
            return grid;
        }
        List<List<Match>> lists = new ArrayList<>(grid.values());
        List<Match> matchListOfLastStage
            = lists.stream().min(Comparator.comparingInt(List::size)).get();
        if (matchListOfLastStage.size() == 1) return grid;
        tournament.addMatches(matchService.createAndSaveMatches(
            matchService.getWinnersOfMatches(matchListOfLastStage)));
        return getGrid(save(tournament));
    }

    @Override
    public Tournament addParticipantToTournamentWithId(Long tournamentId, String participantNickName) {
        Tournament tournament = getById(tournamentId);
        if (tournament.getParticipants().size() < tournament.getMaxNumberParticipants()) {
            if (tournament.getMatches().size() == 0) {
                Participant participant
                    = participantService.getOrSaveIfNotExist(participantNickName);
                tournament.addParticipant(participant);
                return save(tournament);
            }
            if (tournament.getParticipants().size() / 2
                + tournament.getParticipants().size() % 2 == tournament.getMatches().size()) {
                Participant participant
                    = participantService.getOrSaveIfNotExist(participantNickName);
                tournament.addParticipant(participant);
                Match match = tournament.getMatches().get(tournament.getMatches().size() - 1);
                if (match.getSecondParticipant() == null) {
                    match.setSecondParticipant(participant);
                } else {
                    match = new Match();
                    match.setFirstParticipant(participant);
                    tournament.addMatch(matchService.save(match));
                }
            } else {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "You can't add new participants on this stage of tournaments");
            }
            return save(tournament);
        } else {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Tournament are fully");
        }
    }

    @Override
    public void deleteParticipantFromTournament(Long tournamentId,
                                                      String participantNickName) {
        Tournament tournament = getById(tournamentId);
        if (!tournament.getPaused()) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "You can't delete participants from tournament(id="
                    + tournamentId + ") because it is't suspended");
        }
        Participant participant = participantService.getByNickName(participantNickName);
        tournament.deleteParticipantFromTournament(participant);

        if (tournament.getMatches().size() == 0) {
            save(tournament);
            return;
        }

        List<Match> matchesToRemove = tournament.getMatches()
            .stream()
            .filter(match -> match.getFirstParticipant().equals(participant)
                || (match.getSecondParticipant() != null && match.getSecondParticipant().equals(participant)))
            .collect(Collectors.toList());

        List<Participant> participants = matchesToRemove.stream()
            .filter(match -> match.getFirstParticipantScore() == null
            && match.getSecondParticipantScore() == null
            && match.getSecondParticipant() != null)
            .map(match -> match.getFirstParticipant().equals(participant)
                ? match.getSecondParticipant() : match.getFirstParticipant())
            .collect(Collectors.toList());

        tournament.deleteAllMatches(matchesToRemove);
        Match lastMatch = tournament.getMatches().get(tournament.getMatches().size() - 1);
        if (lastMatch.getSecondParticipant() == null) {
            lastMatch.setSecondParticipant(participants.get(participants.size() - 1));
            tournament.addMatches(matchService.createAndSaveMatches(participants.subList(0, participants.size() - 1)));
        } else {
            tournament.addMatches(matchService.createAndSaveMatches(participants));
        }
        save(tournament);
    }
}

