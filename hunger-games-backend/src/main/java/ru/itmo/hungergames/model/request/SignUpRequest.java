package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

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
