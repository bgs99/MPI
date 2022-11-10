package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.Tribute;

import java.util.List;

@Repository
public interface TributeRepository extends JpaRepository<Tribute, Long> {
    List<Tribute> findByName(String name);
}
