package ru.itmo.hungerGames.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApproveResourcesRequest {
    private Long mentorId;
    private Long orderId;
    private Boolean approved = false;
}
