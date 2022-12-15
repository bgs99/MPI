package ru.itmo.hungergames.model.entity.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ResourceOrder")
@Table(name = "resource_orders", indexes = @Index(columnList = "tribute_user_id"))
@SuperBuilder
public class ResourceOrder extends Orders {
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderDetail> orderDetails;
    @ManyToOne
    private Sponsor sponsor;
    @ManyToOne
    private Tribute tribute;
}
