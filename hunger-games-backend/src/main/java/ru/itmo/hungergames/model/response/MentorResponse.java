package ru.itmo.hungergames.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.hungergames.model.entity.Mentor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MentorResponse {
    private UUID id;
    private String name;
    private String username;
    private int district;
    public MentorResponse(Mentor mentor) {
        this.id = mentor.getId();
        this.name = mentor.getName();
        this.username = mentor.getUsername();
        this.district = mentor.getDistrict();
    }
}
