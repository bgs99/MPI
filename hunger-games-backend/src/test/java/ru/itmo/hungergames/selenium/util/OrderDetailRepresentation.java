package ru.itmo.hungergames.selenium.util;

import lombok.AllArgsConstructor;
import ru.itmo.hungergames.model.entity.order.OrderDetail;

@AllArgsConstructor
public class OrderDetailRepresentation {
    public final String name;
    public final int amount;

    @Override
    public String toString() {
        return String.format("%dX %s", this.amount, this.name);
    }

    public boolean matches(OrderDetail detail) {
        return this.amount == detail.getSize() && this.name.equals(detail.getResource().getName());
    }
}
