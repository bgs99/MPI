package ru.itmo.hungergames.model.response;

import lombok.*;
import ru.itmo.hungergames.model.entity.order.ResourceOrder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ResourceApprovedAndNotPaidResponse {
    private UUID orderId;
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

