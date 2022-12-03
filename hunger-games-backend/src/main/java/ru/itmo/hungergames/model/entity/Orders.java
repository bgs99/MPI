package ru.itmo.hungergames.model.entity;

import jakarta.persistence.*;
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
@SuperBuilder
@Table(indexes = @Index(columnList = "tribute_user_id"), name = "orders")
@Inheritance(strategy = InheritanceType.JOINED)
public class Orders {
    @Id
    @Column(name = "order_id")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    private UUID id;
    private Boolean approved;
    @Builder.Default
    private boolean paid = false;
    private BigDecimal price;
    @ManyToOne
    private Tribute tribute;
}
