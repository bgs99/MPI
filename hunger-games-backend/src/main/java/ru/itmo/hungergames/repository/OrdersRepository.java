package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.Orders;
import ru.itmo.hungergames.model.entity.OrdersType;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByTribute_MentorIdAndPaidAndApprovedAndOrdersType(Long mentorId, boolean paid, Boolean approved, OrdersType ordersType);
    Optional<Orders> findByIdAndTribute_MentorIdAndPaid(Long id, Long mentorId, boolean paid);
}
