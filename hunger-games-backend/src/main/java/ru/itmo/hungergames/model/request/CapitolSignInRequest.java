package ru.itmo.hungergames.model.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapitolSignInRequest {
    @NotBlank(message="Username should not be blank")
    @Size(min=3,max=250)
    private String username;
}
