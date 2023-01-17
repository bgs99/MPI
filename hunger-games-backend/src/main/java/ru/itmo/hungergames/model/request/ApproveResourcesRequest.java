package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ApproveResourcesRequest {
    @NotBlank(message="OrderId should not be blank")
    private UUID orderId;
    private Boolean approved = false;
}
