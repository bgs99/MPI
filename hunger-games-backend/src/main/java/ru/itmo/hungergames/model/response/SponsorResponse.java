package ru.itmo.hungergames.model.response;

import lombok.experimental.SuperBuilder;
import ru.itmo.hungergames.model.entity.user.Sponsor;

@SuperBuilder
public class SponsorResponse extends UserResponse {
    public SponsorResponse(Sponsor sponsor) {
        super(sponsor);
    }
}
