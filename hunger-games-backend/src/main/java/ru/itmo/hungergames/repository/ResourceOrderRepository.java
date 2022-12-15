package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.order.ResourceOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourceOrderRepository extends JpaRepository<ResourceOrder, UUID> {
    List<ResourceOrder> findAllByTribute_MentorIdAndPaidAndApproved(UUID mentorId, boolean paid, Boolean approved);
    Optional<ResourceOrder> findByIdAndTribute_MentorIdAndPaid(UUID id, UUID mentorId, boolean paid);
    List<ResourceOrder> findAllByPaidAndApproved(boolean paid, Boolean approved);
}
