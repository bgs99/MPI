package ru.itmo.hungergames.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="AdvertisementOrder")
@Table(name = "advertisement_orders")
@SuperBuilder
public class AdvertisementOrder extends Orders {
    private String advertisingText;
}
