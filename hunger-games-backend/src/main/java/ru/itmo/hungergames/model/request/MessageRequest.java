package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    @NotBlank(message="Message should not be blank")
    @Size(min=1,max=255)
    private String message;
}
