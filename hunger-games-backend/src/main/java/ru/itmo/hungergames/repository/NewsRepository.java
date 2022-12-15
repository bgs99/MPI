package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.News;

import java.util.UUID;

@Repository
public interface NewsRepository extends JpaRepository<News, UUID> {
}
