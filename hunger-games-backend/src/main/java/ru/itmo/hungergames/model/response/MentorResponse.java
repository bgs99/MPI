package ru.itmo.hungergames.model.response;

import lombok.experimental.SuperBuilder;
import ru.itmo.hungergames.model.entity.user.Mentor;

@SuperBuilder
public class MentorResponse extends UserResponse {
    private int district;
    public MentorResponse(Mentor mentor) {
        super(mentor);
        this.district = mentor.getDistrict();
    }
}
