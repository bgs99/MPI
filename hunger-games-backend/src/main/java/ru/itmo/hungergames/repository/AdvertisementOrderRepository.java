package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.AdvertisementOrder;

@Repository
public interface AdvertisementOrderRepository extends JpaRepository<AdvertisementOrder, Long> {
}
