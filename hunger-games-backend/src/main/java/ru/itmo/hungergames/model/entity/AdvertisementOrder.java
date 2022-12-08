package ru.itmo.hungergames.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Getter
@Setter
@NoArgsConstructor
@Entity(name="AdvertisementOrder")
@Table(name = "advertisement_orders")
@SuperBuilder
public class AdvertisementOrder extends Orders {
    @NotNull
    private String advertisingText;
}
