package ru.itmo.hungergames.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceOrderRequest {
    private UUID tributeId;
    private List<OrderDetailRequest> orderDetails;
}
