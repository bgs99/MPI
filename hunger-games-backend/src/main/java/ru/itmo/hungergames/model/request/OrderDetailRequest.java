package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRequest {
    @NotBlank(message = "resource id should not be blank")
    private UUID resourceId;
    @Min(1)
    private int size;
}
