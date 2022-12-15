package ru.itmo.hungergames.model.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.itmo.hungergames.model.entity.order.NewsSubscriptionOrder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Sponsor")
@Table(name = "sponsors")
@SuperBuilder
@JsonIgnoreProperties(
        value = {"password", "userRoles", "authorities",
                "accountNonExpired", "accountNonLocked",
                "credentialsNonExpired", "enabled"})
public class Sponsor extends User {
    @OneToOne
    private NewsSubscriptionOrder newsSubscriptionOrder;
    public Sponsor(String username, String password, String name, String avatarUri) {
        this.setUsername(username);
        this.setPassword(password);
        this.setName(name);
        this.setAvatarUri(avatarUri);
    }
}