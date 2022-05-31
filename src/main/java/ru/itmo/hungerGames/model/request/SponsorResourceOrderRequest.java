package ru.itmo.hungerGames.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.hungerGames.model.entity.OrderDetail;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SponsorResourceOrderRequest {
    Long tributeId;
    Long sponsorId;
    List<OrderDetailRequest> orderDetails;
}
