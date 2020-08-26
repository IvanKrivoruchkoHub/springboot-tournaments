package com.example.springboottournaments;

import com.example.springboottournaments.entity.Match;
import com.example.springboottournaments.entity.Participant;
import com.example.springboottournaments.repository.MatchRepository;
import com.example.springboottournaments.service.iml.MatchServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MatchServiceImplTest {
    @InjectMocks
    private MatchServiceImpl matchServiceImpl;
    @Mock
    private MatchRepository matchRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetById() {
        Match match = new Match();
        match.setId(1l);
        Mockito.when(matchRepository.findById(1L)).thenReturn(Optional.of(match));

        Match actualMatch = matchServiceImpl.getById(1L);
        Assert.assertEquals(Long.valueOf(1L), actualMatch.getId());
    }

    @Test
    public void testSave() {
        Match matchBeforeSave = new Match();
        Match matchAfterSave = new Match();
        matchAfterSave.setId(1l);
        Mockito.when(matchRepository.save(matchBeforeSave)).thenReturn(matchAfterSave);

        Match actualMatch = matchServiceImpl.save(matchBeforeSave);
        Assert.assertNotNull(actualMatch.getId());
    }

    @Test
    public void testGetWinnersOfMatches() {
        List<Match> matches = new ArrayList<>();
        Participant participant1 = new Participant();
        participant1.setNickName("Barcelona");

        Participant participant2 = new Participant();
        participant2.setNickName("Real Madrid");

        Match match = new Match();
        match.setFirstParticipant(participant1);
        match.setSecondParticipant(participant2);
        match.setFirstParticipantScore(2);
        match.setSecondParticipantScore(1);
        matches.add(match);

        Match match2 = new Match();
        match2.setFirstParticipant(participant1);
        matches.add(match2);

        List<Participant> actual = matchServiceImpl.getWinnersOfMatches(matches);
        Assert.assertEquals(actual.size(), matches.size());
        List<Participant> expected = new ArrayList<>();
        expected.add(participant1);
        expected.add(participant1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSaveAll() {
        List<Match> matchesBeforeSave = new ArrayList<>();
        matchesBeforeSave.add(new Match());
        matchesBeforeSave.add(new Match());

        List<Match> matchesAfterSave = new ArrayList<>();
        Match match1 = new Match();
        match1.setId(1L);
        matchesAfterSave.add(match1);
        Match match2 = new Match();
        match2.setId(2L);
        matchesAfterSave.add(match2);

        Mockito.when(matchRepository.saveAll(matchesBeforeSave)).thenReturn(matchesAfterSave);

        List<Match> actual = matchServiceImpl.saveAll(matchesBeforeSave);
        Assert.assertNotNull(actual);
        Assert.assertEquals(matchesBeforeSave.size(), actual.size());
        for (Match m : actual) {
            Assert.assertNotNull(m.getId());
        }
    }

}
