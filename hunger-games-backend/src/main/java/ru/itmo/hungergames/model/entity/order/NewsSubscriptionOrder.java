package ru.itmo.hungergames.model.entity.order;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.itmo.hungergames.model.entity.user.Sponsor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="NewsSubscriptionOrder")
@Table(name = "news_subscription_orders")
@SuperBuilder
public class NewsSubscriptionOrder extends Orders {
    @OneToOne
    private Sponsor sponsor;
    @Email
    private String email;
}
