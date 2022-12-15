package ru.itmo.hungergames.model.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewsSubscriptionOrderRequest {
    @Email
    private String email;
}
