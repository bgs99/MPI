package ru.itmo.hungergames.model.request;

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
    private UUID orderId;
    private Boolean approved = false;
}
