package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.ResourceOrder;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceOrderRepository extends JpaRepository<ResourceOrder, Long> {
    List<ResourceOrder> findAllByTribute_MentorIdAndPaidAndApproved(Long mentorId, boolean paid, Boolean approved);
    Optional<ResourceOrder> findByIdAndTribute_MentorIdAndPaid(Long id, Long mentorId, boolean paid);
    List<ResourceOrder> findAllByPaidAndApproved(boolean paid, Boolean approved);
}