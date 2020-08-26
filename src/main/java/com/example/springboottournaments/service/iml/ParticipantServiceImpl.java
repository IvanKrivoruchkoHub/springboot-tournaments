package com.example.springboottournaments.service.iml;

import com.example.springboottournaments.entity.Participant;
import com.example.springboottournaments.repository.ParticipantRepository;
import com.example.springboottournaments.service.ParticipantService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public List<Participant> createParticipants(List<String> nicknames) {
        List<Participant> participants = new ArrayList<>();
        for (String nickname : nicknames) {
            Participant participant = new Participant();
            participant.setNickName(nickname);
            participants.add(participant);
        }
        return participants;
    }

    @Override
    public List<Participant> saveAll(List<Participant> participants) {
        List<Participant> result = new ArrayList<>();
        for (Participant participant: participants) {
            result.add(getOrSaveIfNotExist(participant.getNickName()));
        }
        return result;
    }

    @Override
    public Participant getOrSaveIfNotExist(String nickname) {
        return participantRepository.findByNickName(nickname).orElseGet(() -> createAndSave(nickname));
    }

    @Override
    public Participant createAndSave(String nickname) {
        Participant participant = new Participant();
        participant.setNickName(nickname);
        return participantRepository.save(participant);
    }

    @Override
    public Participant getByNickName(String nickName) {
        return participantRepository.findByNickName(nickName).get();
    }
}
