package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.Tribute;

import java.util.List;
import java.util.UUID;

@Repository
public interface TributeRepository extends JpaRepository<Tribute, UUID> {
    List<Tribute> findByName(String name);
    List<Tribute> findAllByMentor_Id(UUID mentorId);
}
