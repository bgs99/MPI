package ru.itmo.hungergames.model.response;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.itmo.hungergames.model.entity.user.Mentor;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MentorResponse extends UserResponse {
    private int district;
    public MentorResponse(Mentor mentor) {
        super(mentor);
        this.district = mentor.getDistrict();
    }
}
