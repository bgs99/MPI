package ru.itmo.hungerGames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungerGames.model.entity.Sponsor;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
}
