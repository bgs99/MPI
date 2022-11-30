package ru.itmo.hungergames.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.hungergames.model.entity.Sponsor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SponsorResponse {
    private UUID id;
    private String username;
    private String name;
    public SponsorResponse(Sponsor sponsor) {
        this.id = sponsor.getId();
        this.username = sponsor.getUsername();
        this.name = sponsor.getName();
    }
}
