package ru.itmo.hungergames.selenium.unit;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.selenium.pages.SponsorTributesPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.TributeService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@SeleniumTest
public class SponsorTributesPageTests extends SeleniumTestBase {
    private SponsorTributesPage page;

    @MockBean
    private TributeService tributeService;

    @BeforeEach
    public void setUp() {
        this.page = new SponsorTributesPage(this.driver);

        Sponsor sponsor = Sponsor.builder()
                .id(new UUID(42, 42))
                .name("sponsor")
                .username("test")
                .password("test")
                .build();

        this.authenticate(sponsor, UserRole.SPONSOR);
    }

    @Test
    public void tributesList() {
        var tributes = List.of(Tribute.builder().name("tribute1").build(), Tribute.builder().name("tribute2").build());
        when(tributeService.getAllTributes()).thenReturn(tributes.stream().map(TributeResponse::new).collect(Collectors.toList()));

        this.get("/sponsor/tributes");

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

        this.get("/sponsor/tributes");


        page.waitUntilTributesLoaded();

        var tributeRow = this.page.getTributeRows().get(tribute.getName());

        var sourceUrl = driver.getCurrentUrl();

        tributeRow.getSelectButton().click();

        this.redirectWait(sourceUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("#/sponsor/tribute/" + tribute.getId().toString()));
    }

}
