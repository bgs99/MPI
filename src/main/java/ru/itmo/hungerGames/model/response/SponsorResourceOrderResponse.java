package ru.itmo.hungerGames.model.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SponsorResourceOrderResponse {
    private long id;
    private BigDecimal price;
}
