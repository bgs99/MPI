package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.user.Sponsor;

import java.util.List;
import java.util.UUID;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, UUID> {
    void deleteByUsername(String username);
    List<Sponsor> findAllByNewsSubscriptionOrder_paid(boolean paid);
}
