package ru.itmo.hungerGames.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceApprovalRequest {
    private String name;
    private String sponsorName;
}
