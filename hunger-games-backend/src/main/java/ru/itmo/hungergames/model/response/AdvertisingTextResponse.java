package ru.itmo.hungergames.model.response;

import lombok.*;

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
}
