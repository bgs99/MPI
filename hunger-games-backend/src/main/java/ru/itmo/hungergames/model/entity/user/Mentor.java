package ru.itmo.hungergames.model.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "mentors")
@Entity(name="Mentor")
@JsonIgnoreProperties(
        value = {"password", "userRoles", "authorities",
                "accountNonExpired", "accountNonLocked",
                "credentialsNonExpired", "enabled"})
public class Mentor extends User {
    @Size(min = 1, max = 12)
    private int district;
}
