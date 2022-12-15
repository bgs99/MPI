package ru.itmo.hungergames.model.entity.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.itmo.hungergames.model.entity.user.Tribute;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="AdvertisementOrder")
@Table(name = "advertisement_orders", indexes = @Index(columnList = "tribute_user_id"))
@SuperBuilder
public class AdvertisementOrder extends Orders {
    @NotNull
    private String advertisingText;
    @ManyToOne
    private Tribute tribute;
}
