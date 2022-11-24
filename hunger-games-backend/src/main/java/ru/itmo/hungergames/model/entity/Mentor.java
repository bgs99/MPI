package ru.itmo.hungergames.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="Mentor")
@JsonIgnoreProperties(
        value = {"password", "userRoles", "authorities",
                "accountNonExpired", "accountNonLocked",
                "credentialsNonExpired", "enabled"})
public class Mentor extends User{
    private int district;
}
