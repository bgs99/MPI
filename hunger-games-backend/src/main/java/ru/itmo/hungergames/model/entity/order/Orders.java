package ru.itmo.hungergames.model.entity.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
public class Orders {
    @Id
    @Column(name = "order_id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;
    @Builder.Default
    private Boolean approved = null;
    @Builder.Default
    private boolean paid = false;
    @NotNull
    private BigDecimal price;
}
