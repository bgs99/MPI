package ru.itmo.hungerGames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.hungerGames.model.Tribute;
import ru.itmo.hungerGames.repository.TributeRepository;
import ru.itmo.hungerGames.service.TributeService;

import java.util.List;

@Service
public class TributeServiceImpl implements TributeService {
    private final TributeRepository tributeRepository;

    @Autowired
    public TributeServiceImpl(TributeRepository tributeRepository) {
        this.tributeRepository = tributeRepository;
    }

    @Override
    public List<Tribute> getAllTributes() {
        return tributeRepository.findAll();
    }
}
