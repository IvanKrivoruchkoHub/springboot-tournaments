package com.example.springboottournaments;

import com.example.springboottournaments.entity.Participant;
import com.example.springboottournaments.repository.ParticipantRepository;
import com.example.springboottournaments.service.iml.ParticipantServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ParticipantServiceImplTest {
    @InjectMocks
    private ParticipantServiceImpl participantServiceImpl;
    @Mock
    private ParticipantRepository participantRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateParticipantsFromEmptyListOfNickNames() {
        List<Participant> actual = participantServiceImpl.createParticipants(new ArrayList<>());
        Assert.assertEquals(0, actual.size());
    }

    @Test
    public void testCreateParticipantsFromNotEmptyListOfNickNames() {
        List<String> nickNames = new ArrayList<>();
        nickNames.add("a");
        nickNames.add("b");
        nickNames.add("c");
        List<Participant> actual = participantServiceImpl.createParticipants(nickNames);
        Assert.assertEquals(nickNames.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(nickNames.get(i), actual.get(i).getNickName());
        }
    }

    @Test
    public void testCreateAndSave() {
        String nickName = "a";
        Participant participantBeforeSave = new Participant();
        participantBeforeSave.setNickName(nickName);
        Participant participantAfterSave = new Participant();
        participantAfterSave.setId(1L);
        participantAfterSave.setNickName(nickName);
        Mockito.when(participantRepository.save(participantBeforeSave)).thenReturn(participantAfterSave);

        Participant participant = participantServiceImpl.createAndSave(nickName);
        Assert.assertNotNull(participant.getId());
        Assert.assertEquals(nickName, participant.getNickName());
    }

    @Test
    public void testGetByNickName() {
        String nickName = "a";
        Participant participant = new Participant();
        participant.setId(1L);
        participant.setNickName(nickName);
        Mockito.when(participantRepository.findByNickName(nickName))
            .thenReturn(java.util.Optional.of(participant));
        Participant actual = participantServiceImpl.getByNickName(nickName);
        Assert.assertNotNull(actual.getId());
        Assert.assertEquals(actual.getNickName(), nickName);
    }
}
