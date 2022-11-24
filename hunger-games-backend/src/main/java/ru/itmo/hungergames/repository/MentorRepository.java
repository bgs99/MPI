package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.Mentor;

import java.util.UUID;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, UUID> {
}
