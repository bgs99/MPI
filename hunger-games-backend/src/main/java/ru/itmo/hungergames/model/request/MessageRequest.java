package ru.itmo.hungergames.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    @NotBlank(message="Message should not be blank")
    @Size(min=1,max=255)
    private String message;
}
