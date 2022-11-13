package ru.itmo.hungergames.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private Long id;
    private String token;
    private final String type = "Bearer";
    private String username;
    private Set<String> roles;
}
