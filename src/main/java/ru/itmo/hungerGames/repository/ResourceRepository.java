package ru.itmo.hungerGames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungerGames.model.entity.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
