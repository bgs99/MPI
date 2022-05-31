package ru.itmo.hungerGames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungerGames.model.entity.Mentor;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
}
