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
    private boolean approved = false;
    private BigDecimal price;
    @ManyToOne
    private Tribute tribute;

    @OneToMany
    private List<OrderDetail> orderDetails;
}
