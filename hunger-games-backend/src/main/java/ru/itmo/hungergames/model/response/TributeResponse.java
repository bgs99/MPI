package ru.itmo.hungergames.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.itmo.hungergames.model.entity.user.Tribute;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class TributeResponse extends UserResponse {
    private int district;
    public TributeResponse(Tribute tribute) {
        super(tribute);
        this.district = tribute.getMentor().getDistrict();
    }
}
