package ru.itmo.hungerGames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungerGames.model.entity.Orders;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByTribute_MentorIdAndPaid(Long mentorId, boolean paid);
    Optional<Orders> findByIdAndTribute_MentorIdAndPaid(Long id, Long mentorId, boolean paid);
}
