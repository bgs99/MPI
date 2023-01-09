package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ResourceOrderRequest {
    @NotBlank(message = "Tribute Id should not be blank")
    private UUID tributeId;
    @NotBlank(message = "Order Details should not be blank")
    private List<OrderDetailRequest> orderDetails;
}
