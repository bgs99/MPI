package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    private String content;
}
