package ru.itmo.hungergames.model.entity.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_details")
@AllArgsConstructor
@Builder
public class OrderDetail {
    @Id
    @Column(name = "order_detail_id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;
    @Min(1)
    private int size;

    @OneToOne
    @NotNull
    private Resource resource;

    public BigDecimal getTotal() {
        return this.resource.getPrice().multiply(new BigDecimal(this.size));
    }
}
