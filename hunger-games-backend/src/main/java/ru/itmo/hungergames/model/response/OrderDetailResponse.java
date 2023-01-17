package ru.itmo.hungergames.model.response;

import lombok.*;
import ru.itmo.hungergames.model.entity.order.OrderDetail;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class OrderDetailResponse {
    private String name;
    private int size;

    public OrderDetailResponse(OrderDetail orderDetail) {
        this.name = orderDetail.getResource().getName();
        this.size = orderDetail.getSize();
    }
}
