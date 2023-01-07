package ru.itmo.hungergames.selenium.unit;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.security.JwtFilter;
import ru.itmo.hungergames.selenium.pages.SponsorTributesPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.Utils;
import ru.itmo.hungergames.service.TributeService;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@SeleniumTest
public class SponsorTributesPageTests {
    private SponsorTributesPage page;

    @Autowired
    private WebDriver driver;

    @MockBean
    private TributeService tributeService;

    @SpyBean
    private JwtFilter jwtFilter;

    @SpyBean
    private SecurityUtil securityUtil;

    @BeforeEach
    public void setUp() {
        this.page = new SponsorTributesPage(this.driver);

        Sponsor sponsor = Sponsor.builder()
                .id(new UUID(42, 42))
                .name("sponsor")
                .username("test")
                .password("test")
                .build();

        Utils.authenticate(driver, sponsor, UserRole.SPONSOR, this.jwtFilter, this.securityUtil);
    }

    @Test
    public void tributesList() {
        var tributes = List.of(Tribute.builder().name("tribute1").build(), Tribute.builder().name("tribute2").build());
        when(tributeService.getAllTributes()).thenReturn(tributes.stream().map(TributeResponse::new).collect(Collectors.toList()));

        driver.get("localhost:42322/#/sponsor/tributes");

        page.waitUntilTributesLoaded();

        var tributeRows = this.page.getTributeRows();

        var names = tributeRows.keySet();
        var expectedNames = tributes.stream().map(Tribute::getName).collect(Collectors.toSet());

        Assertions.assertEquals(expectedNames, names);
    }

    @Test
    public void redirectToTribute() {
        var tribute = Tribute.builder().name("tribute1").id(new UUID(42, 42)).build();
        when(tributeService.getAllTributes()).thenReturn(List.of(new TributeResponse(tribute)));

        driver.get("localhost:42322/#/sponsor/tributes");


        page.waitUntilTributesLoaded();

        var tributeRow = this.page.getTributeRows().get(tribute.getName());

        var sourceUrl = driver.getCurrentUrl();

        tributeRow.getSelectButton().click();

        Utils.redirectWait(driver, sourceUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("#/sponsor/tribute/" + tribute.getId().toString()));
    }

}
