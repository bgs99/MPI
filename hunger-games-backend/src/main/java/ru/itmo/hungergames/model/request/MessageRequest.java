package ru.itmo.hungergames.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    @NotBlank(message = "Chat id should not be blank")
    private UUID chatId;

    @NotBlank(message="Message should not be blank")
    @Size(min=1,max=255)
    private String message;
}
