package ru.itmo.hungergames.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private UUID id;
    private String token;
    private final String type = "Bearer";
    private String username;
    private Set<String> roles;
    private String name;
}
