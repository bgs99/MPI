package ru.itmo.hungergames.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Sponsor")
@Table(name = "sponsors")
@JsonIgnoreProperties(
        value = {"password", "userRoles", "authorities",
                "accountNonExpired", "accountNonLocked",
                "credentialsNonExpired", "enabled"})
public class Sponsor extends User{
    public Sponsor(String username, String password, String name) {
        this.setUsername(username);
        this.setPassword(password);
        this.setName(name);
    }
}
