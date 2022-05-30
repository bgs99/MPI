package ru.itmo.hungerGames;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TributeRepository extends PagingAndSortingRepository<Tribute, Long> {
    List<Tribute> findByName(@Param("name") String name);
}
