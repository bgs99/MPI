package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApproveResourcesRequest {
    @NotBlank(message="OrderId should not be blank")
    private UUID orderId;
    private Boolean approved = false;
}
