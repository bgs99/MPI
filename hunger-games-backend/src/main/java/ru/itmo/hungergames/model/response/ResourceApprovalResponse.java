package ru.itmo.hungergames.model.response;

import lombok.*;
import ru.itmo.hungergames.model.entity.Orders;
import ru.itmo.hungergames.model.entity.ResourceOrder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceApprovalResponse {
    private Long orderId;
    private String sponsorName;
    private String tributeName;
    private List<OrderDetailResponse> resources;

    public ResourceApprovalResponse(ResourceOrder order) {
        this.orderId = order.getId();
        this.sponsorName = order.getSponsor().getName();
        this.tributeName = order.getTribute().getName();
        this.resources = order.getOrderDetails().stream()
                .map(OrderDetailResponse::new)
                .collect(Collectors.toList());
    }
}
