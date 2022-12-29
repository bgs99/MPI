package ru.itmo.hungergames.model.response;

import lombok.*;
import ru.itmo.hungergames.model.entity.order.AdvertisementOrder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisingTextResponse {
    private UUID orderId;
    private BigDecimal price;

    public AdvertisingTextResponse(AdvertisementOrder advertisementOrder) {
        this.orderId = advertisementOrder.getId();
        this.price = advertisementOrder.getPrice();
    }
}
