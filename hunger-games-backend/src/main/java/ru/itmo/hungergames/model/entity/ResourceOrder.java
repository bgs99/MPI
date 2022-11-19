package ru.itmo.hungergames.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ResourceOrder")
@SuperBuilder
public class ResourceOrder extends Orders {
    @OneToMany
    @JoinColumn(name = "order_id")
    private List<OrderDetail> orderDetails;
    @ManyToOne
    private Sponsor sponsor;
}
