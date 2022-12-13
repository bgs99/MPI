package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.Orders;
import ru.itmo.hungergames.model.request.PaymentRequest;
import ru.itmo.hungergames.repository.OrdersRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@RunWith(SpringRunner.class)
class PaymentManagerServiceImplTest {
    @Autowired
    PaymentManagerServiceImpl paymentManagerService;
    @MockBean
    OrdersRepository ordersRepository;
    @Test
    void approvePayment() {
        UUID id = new UUID(1,1);
        Orders order = Orders.builder().id(id).paid(false).build();
        Mockito.doReturn(Optional.of(order)).when(ordersRepository).findById(id);

        paymentManagerService.approvePayment(new PaymentRequest(id));
        Mockito.verify(ordersRepository, Mockito.times(1)).save(order);
        Mockito.verify(ordersRepository, Mockito.times(1)).findById(id);
        assertTrue(order.isPaid());
    }
    @Test
    void approvePaymentOrderNotExists(){
        UUID id = new UUID(1,1);
        Throwable thrown = catchThrowable(()-> paymentManagerService.approvePayment(new PaymentRequest(id)));
        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertThat(thrown.getMessage()).contains("Order is not found.");
    }
}