package ru.itmo.hungergames.model.response;

import lombok.Builder;
import lombok.experimental.SuperBuilder;
import ru.itmo.hungergames.model.entity.Tribute;

@SuperBuilder
public class TributeResponse extends UserResponse {
    public TributeResponse(Tribute tribute) {
        super(tribute);
    }
}
