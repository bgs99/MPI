package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.user.Settings;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.SettingsChangeRequest;
import ru.itmo.hungergames.model.response.SettingsResponse;
import ru.itmo.hungergames.repository.UserRepository;
import ru.itmo.hungergames.service.UserService;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    SecurityUtil securityUtil;

    @Autowired
    UserService userService;

    private UUID userId;

    private Settings settings;

    private User mockUser;

    @BeforeEach
    public void SetUp() {
        userId = new UUID(42, 42);
        settings = new Settings(userId, "testuser@capitol.com");
        mockUser = User.builder()
                .id(userId)
                .settings(settings)
                .userRoles(Set.of(UserRole.SPONSOR))
                .name("test")
                .username("test")
                .build();

        Mockito.doReturn(userId).when(securityUtil).getAuthenticatedUserId();
        Mockito.doReturn(Optional.of(mockUser)).when(userRepository).findById(userId);
    }

    @Test
    public void changeSettings() {
        SettingsChangeRequest expectedRequest = new SettingsChangeRequest("new@capitol.com");

        userService.changeSettings(expectedRequest);

        assertEquals(expectedRequest.getEmail(), mockUser.getSettings().getEmail());
        Mockito.verify(userRepository, Mockito.times(1)).save(mockUser);
    }

    @Test
    public void changeSettingsNoSuchUser() {
        Mockito.clearInvocations(userRepository);
        Mockito.doReturn(Optional.empty()).when(userRepository).findById(userId);

        SettingsChangeRequest expectedRequest = new SettingsChangeRequest("new@capitol.com");
        Throwable thrown = catchThrowable(() -> userService.changeSettings(expectedRequest));

        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertThat(thrown.getMessage()).contains("There's no user with such ID");
    }

    @Test
    public void getSettings() {
        SettingsResponse settingsResponse = userService.getSettings();

        SettingsResponse expectedResponse = new SettingsResponse(settings);

        assertEquals(expectedResponse, settingsResponse);
    }

    @Test
    public void getSettingsNoSuchUser() {
        Mockito.clearInvocations(userRepository);
        Mockito.doReturn(Optional.empty()).when(userRepository).findById(userId);

        Throwable thrown = catchThrowable(() -> userService.getSettings());

        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertThat(thrown.getMessage()).contains("There's no user with such ID");
    }
}