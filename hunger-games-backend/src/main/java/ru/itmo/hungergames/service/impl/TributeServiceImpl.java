package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.AdvertisementOrder;
import ru.itmo.hungergames.model.entity.Tribute;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.repository.AdvertisementOrderRepository;
import ru.itmo.hungergames.repository.TributeRepository;
import ru.itmo.hungergames.service.TributeService;
import ru.itmo.hungergames.util.SecurityUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TributeServiceImpl implements TributeService {
    private final TributeRepository tributeRepository;
    private final AdvertisementOrderRepository advertisementOrderRepository;
    private final SecurityUtil securityUtil;

    @Autowired
    public TributeServiceImpl(TributeRepository tributeRepository,
                              AdvertisementOrderRepository advertisementOrderRepository,
                              SecurityUtil securityUtil) {
        this.tributeRepository = tributeRepository;
        this.advertisementOrderRepository = advertisementOrderRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public TributeResponse getTributeById(UUID tributeId) {
        return new TributeResponse(tributeRepository
                .findById(tributeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Tribute with id=%s doesn't exist", tributeId))));
    }

    @Override
    public List<TributeResponse> getAllTributes() {
        return tributeRepository
                .findAll().stream()
                .map(TributeResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdvertisingTextResponse sendAdvertisingText(AdvertisingTextRequest advertisingTextRequest) {
        Tribute tribute = tributeRepository
                .findById(securityUtil.getAuthenticatedUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no tribute with such ID"));

        AdvertisementOrder order = advertisementOrderRepository.save(AdvertisementOrder.builder()
                .tribute(tribute)
                .advertisingText(advertisingTextRequest.getText())
                .price(BigDecimal.valueOf(200))
                .build());

        return new AdvertisingTextResponse(
                order.getId(),
                order.getPrice()
        );
    }
}
