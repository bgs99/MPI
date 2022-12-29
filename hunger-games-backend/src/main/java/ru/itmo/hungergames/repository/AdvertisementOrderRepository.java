package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.order.AdvertisementOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdvertisementOrderRepository extends JpaRepository<AdvertisementOrder, UUID> {
    Optional<AdvertisementOrder> findFirstByApproved(Boolean approved);
    List<AdvertisementOrder> findAllByApprovedAndPaid(Boolean approved, Boolean paid);
}
