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
@Table(indexes = @Index(columnList = "tribute_id"))
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Boolean approved;
    @Builder.Default
    private boolean paid = false;
    private BigDecimal price;
    @ManyToOne
    private Tribute tribute;

    @Enumerated(EnumType.STRING)
    private OrdersType ordersType;

    @OneToMany
    @JoinColumn(name = "order_id")
    private List<OrderDetail> orderDetails;
}
