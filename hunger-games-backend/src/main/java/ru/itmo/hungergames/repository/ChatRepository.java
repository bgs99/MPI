package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.Chat;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByTribute_Id(Long tributeId);
    List<Chat> findAllByTribute_Mentor_Id(Long mentorId);
    List<Chat> findAllBySponsor_Id(Long sponsorId);
    Boolean existsAllByTribute_IdAndSponsor_id(Long tributeId, Long sponsorId);
    Boolean existsByIdAndTribute_Id(Long chatId, Long tributeId);
    Boolean existsByIdAndTribute_Mentor_Id(Long chatId, Long mentorId);
    Boolean existsByIdAndSponsor_Id(Long chatId, Long sponsorId);
}
