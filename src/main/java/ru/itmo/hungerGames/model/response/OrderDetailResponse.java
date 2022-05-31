package ru.itmo.hungerGames.model.response;

import lombok.*;
import ru.itmo.hungerGames.model.entity.OrderDetail;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private String name;
    private int size;

    public OrderDetailResponse(OrderDetail orderDetail) {
        this.name = orderDetail.getResource().getName();
        this.size = orderDetail.getSize();
    }
}
