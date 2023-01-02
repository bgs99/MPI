package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.order.AdvertisementOrder;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.request.AdvertisingTextRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextOrderResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.repository.AdvertisementOrderRepository;
import ru.itmo.hungergames.repository.TributeRepository;
import ru.itmo.hungergames.service.TributeService;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
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
    @SpyBean
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
    void getTributeNotExists() {
        UUID id = UUID.randomUUID();
        Throwable thrown = catchThrowable(() -> tributeService.getTributeById(id));
        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertThat(thrown.getMessage()).contains(String.format("Tribute with id=%s doesn't exist", id));
    }

    @Test
    @Sql(value = {"/initScripts/create-multiple-tributes.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-multiple-tributes.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllTributes() {
        List<UUID> initialUUIDs = new ArrayList<>();
        initialUUIDs.add(UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32"));
        initialUUIDs.add(UUID.fromString("c0b91cca-27ba-49d2-85e1-290cbd73d45e"));
        initialUUIDs.add(UUID.fromString("3ee25464-bdec-4237-a335-94c1f376e8d6"));

        List<TributeResponse> tributeResponses = tributeService.getAllTributes();
        List<UUID> uuids = tributeResponses.stream().map(TributeResponse::getId).toList();

        assertTrue(uuids.containsAll(initialUUIDs));
        Mockito.verify(tributeRepository, Mockito.times(1)).findAll();
    }

    @Test
    @Sql(value = {"/initScripts/create-tribute.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-tribute.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void sendAdvertisingText() {
        UUID tributeId = UUID.fromString("9667900f-24b2-4795-ad20-28b933d9ae32");
        Tribute tribute = new Tribute();
        tribute.setId(tributeId);

        Mockito.doReturn(Optional.of(tribute))
                .when(tributeRepository)
                .findById(tributeId);
        Mockito.doReturn(tributeId)
                .when(securityUtil)
                .getAuthenticatedUserId();
        Mockito.doReturn(tribute)
                .when(securityUtil)
                .getAuthenticatedUser();

        AdvertisingTextOrderResponse advertisingTextOrderResponse = tributeService.sendAdvertisingText(new AdvertisingTextRequest("Some Text"));
        AdvertisementOrder advertisementOrder = advertisementOrderRepository.findById(advertisingTextOrderResponse.getOrderId()).orElseThrow();

        assertEquals("Some Text", advertisementOrder.getAdvertisingText());
        assertNotNull(advertisementOrder.getPrice());
        assertNull(advertisementOrder.getApproved());
        assertFalse(advertisementOrder.isPaid());
        advertisementOrderRepository.delete(advertisementOrder);
    }

}