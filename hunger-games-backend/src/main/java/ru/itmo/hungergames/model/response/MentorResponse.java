package ru.itmo.hungergames.model.response;

import lombok.Builder;
import lombok.experimental.SuperBuilder;
import ru.itmo.hungergames.model.entity.Mentor;

@SuperBuilder
public class MentorResponse extends UserResponse {
    private int district;
    public MentorResponse(Mentor mentor) {
        super(mentor);
        this.district = mentor.getDistrict();
    }
}
