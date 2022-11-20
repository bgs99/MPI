package ru.itmo.hungergames.model.response;

import lombok.*;
import ru.itmo.hungergames.model.entity.ResourceOrder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceApprovedAndNotPaidResponse {
    private Long orderId;
    private String sponsorName;
    private String tributeName;
    private List<OrderDetailResponse> resources;

    public ResourceApprovedAndNotPaidResponse(ResourceOrder order) {
        this.orderId = order.getId();
        this.tributeName = order.getTribute().getName();
        this.resources = order.getOrderDetails().stream()
                .map(OrderDetailResponse::new)
                .collect(Collectors.toList());
    }
}
