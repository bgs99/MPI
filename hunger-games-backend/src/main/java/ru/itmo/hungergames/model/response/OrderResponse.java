package ru.itmo.hungergames.model.response;

import lombok.Data;
import ru.itmo.hungergames.model.entity.order.ResourceOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class OrderResponse {
    private UUID orderId;
    private BigDecimal sum;
    private List<OrderDetailResponse> orderDetailResponses;
    private boolean paid;
    private Boolean approved;

    public OrderResponse(ResourceOrder order) {
        this.orderId = order.getId();
        this.sum = order.getPrice();
        this.approved = order.getApproved();
        this.paid = order.isPaid();
        this.orderDetailResponses = order.getOrderDetails().stream()
                .map(OrderDetailResponse::new)
                .collect(Collectors.toList());
    }
}
