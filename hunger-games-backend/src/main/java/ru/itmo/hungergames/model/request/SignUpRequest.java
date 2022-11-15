package ru.itmo.hungergames.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message="Username should not be blank")
    @Size(min=3,max=250)
    private String username;

    @NotBlank(message="Name should not be blank")
    @Size(min=3,max=250)
    private String name;

    @NotBlank(message="Password should not be blank")
    @Size(min=3,max=255)
    private String password;
}
