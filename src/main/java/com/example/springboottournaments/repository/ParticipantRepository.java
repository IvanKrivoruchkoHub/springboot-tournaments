package com.example.springboottournaments.repository;

import com.example.springboottournaments.entity.Participant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByNickName(String nickname);
}
