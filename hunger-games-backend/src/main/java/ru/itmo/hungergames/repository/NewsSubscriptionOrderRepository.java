package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.hungergames.model.entity.order.NewsSubscriptionOrder;

import java.util.UUID;

public interface NewsSubscriptionOrderRepository extends JpaRepository<NewsSubscriptionOrder, UUID> {
}
