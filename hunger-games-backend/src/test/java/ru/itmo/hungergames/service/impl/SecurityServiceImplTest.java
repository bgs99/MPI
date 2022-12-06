package ru.itmo.hungergames.service.impl;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.Sponsor;
import ru.itmo.hungergames.model.entity.UserRole;
import ru.itmo.hungergames.repository.SponsorRepository;
import ru.itmo.hungergames.service.SecurityService;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityServiceImplTest {
    @MockBean
    private BCryptPasswordEncoder encoder;
    @MockBean
    private SecurityUtil securityUtil;
    @MockBean
    private SponsorRepository sponsorRepository;
    @Autowired
    private SecurityService securityService;


    @Test
    void createSponsor() {
        Sponsor sponsor = new Sponsor("sponsor-username", "some-password", "sponsor-name");
        securityService.createSponsor(sponsor);

        assertTrue(CoreMatchers.is(sponsor.getUserRoles()).matches(Collections.singleton(UserRole.SPONSOR)));
        Mockito.verify(encoder, Mockito.times(1)).encode(ArgumentMatchers.anyString());
        Mockito.verify(sponsorRepository, Mockito.times(1)).save(sponsor);
        Mockito.verify(securityUtil, Mockito.times(1)).validateBeforeSigningUp(sponsor);}
}