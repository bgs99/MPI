package ru.itmo.hungergames.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="Tribute")
@JsonIgnoreProperties(
        value = {"password", "userRoles", "authorities",
                "accountNonExpired", "accountNonLocked",
                "credentialsNonExpired", "enabled"})
public class Tribute extends User {
    @ManyToOne
    private Mentor mentor;
}
