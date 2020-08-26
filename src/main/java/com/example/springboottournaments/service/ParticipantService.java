package com.example.springboottournaments.service;

import com.example.springboottournaments.entity.Participant;
import java.util.List;

public interface ParticipantService {
    List<Participant> createParticipants(List<String> participants);
    List<Participant> saveAll(List<Participant> participants);
    Participant getOrSaveIfNotExist(String nickname);
    Participant createAndSave(String nickname);
    Participant getByNickName(String nickName);
}
