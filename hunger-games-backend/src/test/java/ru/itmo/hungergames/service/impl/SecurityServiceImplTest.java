package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import ru.itmo.hungergames.model.entity.user.Settings;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.repository.SettingsRepository;
import ru.itmo.hungergames.repository.SponsorRepository;
import ru.itmo.hungergames.service.SecurityService;

import java.util.Set;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityServiceImplTest {
    @MockBean
    private BCryptPasswordEncoder encoder;
    @MockBean
    private SponsorRepository sponsorRepository;
    @MockBean
    private SettingsRepository settingsRepository;

    @Autowired
    private SecurityService securityService;

    @Test
    void createSponsor() {
        final var sponsorRequest = new Sponsor("sponsor-username", "some-password", "sponsor-name", null);
        
        final var encodedPassword = "encoded-password";

        doReturn(encodedPassword).when(this.encoder).encode(sponsorRequest.getPassword());
        
        securityService.createSponsor(sponsorRequest);

        final var expectedSponsor = Sponsor.builder()
            .username(sponsorRequest.getUsername())
            .password(encodedPassword)
            .name(sponsorRequest.getName())
            .userRoles(Set.of(UserRole.SPONSOR))
            .settings(new Settings())
            .build();

        verify(this.sponsorRepository, times(1)).save(expectedSponsor);
    }
}