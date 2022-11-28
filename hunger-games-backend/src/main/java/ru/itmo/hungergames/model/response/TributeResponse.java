package ru.itmo.hungergames.model.response;

import lombok.*;
import ru.itmo.hungergames.model.entity.Tribute;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TributeResponse {
    private UUID id;
    private String name;

    public TributeResponse(Tribute tribute) {
        this.id = tribute.getId();
        this.name = tribute.getName();
    }
}
