package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCreateRequest {
    @NotBlank(message = "Tribute id should not be blank")
    private UUID tributeId;
}
