package ru.itmo.hungergames.model.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SponsorResourceOrderResponse {
    private long orderId;
    private BigDecimal price;
}
