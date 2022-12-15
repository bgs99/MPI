package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.chat.Chat;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findAllByTribute_Id(UUID tributeId);
    List<Chat> findAllByTribute_Mentor_Id(UUID mentorId);
    List<Chat> findAllBySponsor_Id(UUID sponsorId);
    Boolean existsAllByTribute_IdAndSponsor_id(UUID tributeId, UUID sponsorId);
    Boolean existsByIdAndTribute_Id(UUID chatId, UUID tributeId);
    Boolean existsByIdAndTribute_Mentor_Id(UUID chatId, UUID mentorId);
    Boolean existsByIdAndSponsor_Id(UUID chatId, UUID sponsorId);
}
