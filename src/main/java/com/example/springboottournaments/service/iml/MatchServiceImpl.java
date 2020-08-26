package com.example.springboottournaments.service.iml;

import com.example.springboottournaments.dto.MatchResultDto;
import com.example.springboottournaments.entity.Match;
import com.example.springboottournaments.entity.Participant;
import com.example.springboottournaments.repository.MatchRepository;
import com.example.springboottournaments.service.MatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchServiceImpl implements MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Override
    public List<Match> createAndSaveMatches(List<Participant> participants) {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < participants.size(); i+=2) {
            Match match = new Match();
            match.setFirstParticipant(participants.get(i));
            match.setSecondParticipant(i != participants.size() - 1
                ? participants.get(i+1)
                : null);
            matches.add(match);
        }
        return saveAll(matches);
    }

    @Override
    public List<Match> saveAll(List<Match> matches) {
        return matchRepository.saveAll(matches);
    }

    @Override
    public Match getById(Long id) {
        return matchRepository.findById(id).get();
    }

    @Override
    public Match save(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public List<Participant> getWinnersOfMatches(List<Match> matches) {
        return matches.stream()
            .map(match -> match.getSecondParticipant() == null
                || match.getFirstParticipantScore() > match.getSecondParticipantScore()
                ? match.getFirstParticipant()
                : match.getSecondParticipant())
            .collect(Collectors.toList());
    }
}
