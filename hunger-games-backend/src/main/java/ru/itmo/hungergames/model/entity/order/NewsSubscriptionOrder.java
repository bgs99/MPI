package ru.itmo.hungergames.model.entity.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import ru.itmo.hungergames.model.entity.user.Sponsor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="NewsSubscriptionOrder")
@Table(name = "news_subscription_orders")
@SuperBuilder
public class NewsSubscriptionOrder extends Orders {
    @NotNull
    @Builder.Default
    private BigDecimal price = new BigDecimal(300);
    @OneToOne
    private Sponsor sponsor;
    @Email
    private String email;
}
