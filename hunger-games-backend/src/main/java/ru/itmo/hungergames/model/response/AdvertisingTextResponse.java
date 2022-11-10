package ru.itmo.hungergames.model.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisingTextResponse {
    private Long orderId;
    private BigDecimal price;
}
