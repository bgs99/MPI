package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.AdvertisementOrder;
import ru.itmo.hungergames.model.entity.Tribute;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.repository.AdvertisementOrderRepository;
import ru.itmo.hungergames.repository.TributeRepository;
import ru.itmo.hungergames.service.TributeService;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class TributeServiceImplTest {
    @Autowired
    TributeService tributeService;
    @Autowired
    AdvertisementOrderRepository advertisementOrderRepository;
    @MockBean
    TributeRepository tributeRepository;
    @MockBean
    SecurityUtil securityUtil;
    @Test
    void getTributeById() {
        Tribute tribute = new Tribute();
        UUID id = UUID.randomUUID();
        tribute.setName("name");
        tribute.setId(id);
        Mockito.doReturn(Optional.of(tribute))
                .when(tributeRepository)
                .findById(id);
        TributeResponse tributeById = tributeService.getTributeById(id);
        assertEquals("name", tributeById.getName());
        assertEquals(id, tributeById.getId());
        Mockito.verify(tributeRepository, Mockito.times(1)).findById(id);
    }
    @Test
    void getTributeNotExists(){
        UUID id = UUID.randomUUID();
        Throwable thrown = catchThrowable(()->tributeService.getTributeById(id));
        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertThat(thrown.getMessage()).contains(String.format("Tribute with id=%s doesn't exist", id));
    }

    @Test
    void getAllTributes() {
        tributeService.getAllTributes();
        Mockito.verify(tributeRepository, Mockito.times(1)).findAll();
    }

    @Test
    @Sql(value = {"/initScripts/create-tribute.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-tribute.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void sendAdvertisingText() {
        UUID id = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        Tribute tribute = new Tribute();
        tribute.setId(id);
        Mockito.doReturn(Optional.of(tribute))
                .when(tributeRepository)
                .findById(id);
        Mockito.doReturn(tribute)
                .when(securityUtil)
                .getAuthenticatedUser();
        AdvertisingTextResponse advertisingTextResponse = tributeService.sendAdvertisingText(new AdvertisingTextRequest("Some Text"));
        AdvertisementOrder advertisementOrder = advertisementOrderRepository.findById(advertisingTextResponse.getOrderId()).get();
        assertEquals("Some Text", advertisementOrder.getAdvertisingText());
        assertNotNull(advertisementOrder.getPrice());
        assertNull(advertisementOrder.getApproved());
        assertFalse(advertisementOrder.isPaid());
        advertisementOrderRepository.delete(advertisementOrder);
    }

}