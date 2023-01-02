package ru.itmo.hungergames.model.response;

import lombok.*;
import ru.itmo.hungergames.model.entity.order.AdvertisementOrder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisingTextResponse {
    private UUID orderId;
    private String text;

    public AdvertisingTextResponse(AdvertisementOrder advertisementOrder) {
        this.orderId = advertisementOrder.getId();
        this.text = advertisementOrder.getAdvertisingText();
    }
}
