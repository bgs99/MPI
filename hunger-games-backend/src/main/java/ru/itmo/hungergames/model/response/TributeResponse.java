package ru.itmo.hungergames.model.response;

import lombok.experimental.SuperBuilder;
import ru.itmo.hungergames.model.entity.user.Tribute;

@SuperBuilder
public class TributeResponse extends UserResponse {
    public TributeResponse(Tribute tribute) {
        super(tribute);
    }
}
