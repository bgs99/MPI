package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.Tribute;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextResponse;
import ru.itmo.hungergames.repository.TributeRepository;
import ru.itmo.hungergames.service.TributeService;

import java.util.List;

@Service
@Transactional(readOnly = true)
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

    @Override
    public AdvertisingTextResponse sendAdvertisingText(AdvertisingTextRequest advertisingTextRequest) {
        return null;
    }
}
