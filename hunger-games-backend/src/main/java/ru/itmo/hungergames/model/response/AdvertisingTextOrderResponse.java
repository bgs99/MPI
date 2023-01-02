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
public class AdvertisingTextOrderResponse {
    private UUID orderId;
    private BigDecimal price;

    public AdvertisingTextOrderResponse(AdvertisementOrder advertisementOrder) {
        this.orderId = advertisementOrder.getId();
        this.price = advertisementOrder.getPrice();
    }
}
