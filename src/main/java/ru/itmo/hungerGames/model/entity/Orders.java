package ru.itmo.hungerGames.model.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Builder.Default
    private boolean approved = false;
    @Builder.Default
    private boolean paid = false;
    private BigDecimal price;
    @ManyToOne
    private Tribute tribute;

    @Enumerated(EnumType.STRING)
    private OrdersType ordersType;

    @OneToMany
    private List<OrderDetail> orderDetails;
}
