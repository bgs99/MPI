package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrderDetailRequest {
    @NotBlank(message = "resource id should not be blank")
    private UUID resourceId;
    @Min(1)
    private int size;
}
