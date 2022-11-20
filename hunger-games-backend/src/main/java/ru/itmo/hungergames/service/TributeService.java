package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.entity.Tribute;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextResponse;

import java.util.List;

public interface TributeService {
    List<Tribute> getAllTributes();
    AdvertisingTextResponse sendAdvertisingText(AdvertisingTextRequest advertisingTextRequest);
}
