package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.Orders;
import ru.itmo.hungergames.model.entity.OrdersType;
import ru.itmo.hungergames.model.entity.Tribute;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextResponse;
import ru.itmo.hungergames.repository.OrdersRepository;
import ru.itmo.hungergames.repository.TributeRepository;
import ru.itmo.hungergames.service.TributeService;
import ru.itmo.hungergames.util.SecurityUtil;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TributeServiceImpl implements TributeService {
    private final TributeRepository tributeRepository;
    private final OrdersRepository ordersRepository;
    private final SecurityUtil securityUtil;

    @Autowired
    public TributeServiceImpl(TributeRepository tributeRepository, OrdersRepository ordersRepository, SecurityUtil securityUtil) {
        this.tributeRepository = tributeRepository;
        this.ordersRepository = ordersRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public List<Tribute> getAllTributes() {
        return tributeRepository.findAll();
    }

    @Override
    @Transactional
    public AdvertisingTextResponse sendAdvertisingText(AdvertisingTextRequest advertisingTextRequest) {
        Tribute tribute = tributeRepository
                .findById(securityUtil.getAuthenticatedUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no tribute with such ID"));

        Orders order = ordersRepository.save(Orders.builder()
                .tribute(tribute)
                .advertisingText(advertisingTextRequest.getText())
                .ordersType(OrdersType.ADVERTISEMENT)
                .price(BigDecimal.valueOf(200))
                .build());

        return new AdvertisingTextResponse(
                order.getId(),
                order.getPrice()
        );
    }
}
