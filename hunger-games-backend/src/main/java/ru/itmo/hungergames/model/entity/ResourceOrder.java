package ru.itmo.hungergames.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ResourceOrder")
@Table(name = "resource_orders")
@SuperBuilder
public class ResourceOrder extends Orders {
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderDetail> orderDetails;
    @ManyToOne
    private Sponsor sponsor;
}
