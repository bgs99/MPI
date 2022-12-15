package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.order.Resource;
import ru.itmo.hungergames.repository.ResourceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
class ResourceServiceImplTest {
    @Autowired
    ResourceServiceImpl resourceService;
    @SpyBean
    ResourceRepository resourceRepository;

    @Test
    @Sql(value = {"/initScripts/create-resources.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/initScripts/drop-resources.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllResources() {
        List<UUID> initialUUIDs = new ArrayList<>();
        initialUUIDs.add(UUID.fromString("33ff5ee9-c0d7-4955-b2cd-a0aa3d484b98"));
        initialUUIDs.add(UUID.fromString("47f75e81-4f14-4af5-bce2-b6d5af372d94"));
        initialUUIDs.add(UUID.fromString("45a95e19-e0e9-460e-9cba-bcd275e717a3"));

        List<Resource> resources = resourceService.getAllResources();
        List<UUID> uuids = resources.stream().map(Resource::getId).toList();

        assertTrue(uuids.containsAll(initialUUIDs));
        Mockito.verify(resourceRepository, Mockito.times(1)).findAll();
    }
}