package ru.itmo.hungergames.selenium.util;

import lombok.AllArgsConstructor;
import ru.itmo.hungergames.model.entity.order.ResourceOrder;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OrderRepresentation {
    public final List<OrderDetailRepresentation> details;

    @Override
    public String toString() {
        return this.details.stream().map(OrderDetailRepresentation::toString).collect(Collectors.joining("\n"));
    }

    public boolean matches(ResourceOrder order) {
        final var orderDetails = order.getOrderDetails();
        if (this.details.size() != orderDetails.size()) {
            return false;
        }
        for (int i = 0; i < this.details.size(); ++i) {
            if (!this.details.get(i).matches(orderDetails.get(i))) {
                return false;
            }
        }
        return true;
    }
}
