package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AdvertisingTextRequest {
    @NotBlank(message="Advertising text should not be blank")
    @Size(min=10,max=3500)
    private String text;
}
