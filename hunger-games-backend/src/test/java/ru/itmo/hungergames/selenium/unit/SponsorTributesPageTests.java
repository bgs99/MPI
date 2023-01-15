package ru.itmo.hungergames.selenium.unit;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.user.Mentor;
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

    @MockBean
    private TributeService tributeService;

    @BeforeEach
    public void setUp() {
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
        var mentor1 = Mentor.builder()
                .district(1)
                .build();
        var mentor2 = Mentor.builder()
                .district(2)
                .build();

        var tribute1 = Tribute.builder()
                .name("tribute1")
                .mentor(mentor1)
                .build();

        var tribute2 = Tribute.builder()
                .name("tribute2")
                .mentor(mentor2)
                .build();

        var tributes = List.of(tribute1, tribute2);
        when(tributeService.getAllTributes()).thenReturn(tributes.stream().map(TributeResponse::new).collect(Collectors.toList()));

        final var page = this.getInit("/sponsor/tributes", SponsorTributesPage.class);

        var tributeRows = page.getTributeRows();

        var names = tributeRows.stream().map(SponsorTributesPage.TributeRow::getName).collect(Collectors.toList());
        var expectedNames = tributes.stream().map(Tribute::getName).collect(Collectors.toList());

        Assertions.assertEquals(expectedNames, names);

        var districts = tributeRows.stream().map(SponsorTributesPage.TributeRow::getDistrict).collect(Collectors.toList());
        var expectedDistricts = tributes.stream().map(Tribute::getMentor).map(Mentor::getDistrict).collect(Collectors.toList());

        Assertions.assertEquals(expectedDistricts, districts);
    }

    @Test
    public void redirectToTribute() {
        var mentor = Mentor.builder().district(2).build();
        var tribute = Tribute.builder()
                .name("tribute1")
                .id(new UUID(42, 42))
                .mentor(mentor)
                .build();
        doReturn(List.of(new TributeResponse(tribute))).when(tributeService).getAllTributes();

        final var page = this.getInit("/sponsor/tributes", SponsorTributesPage.class);

        var tributeRow = page.getTributeRows().get(0);

        var sourceUrl = driver.getCurrentUrl();

        tributeRow.getSelectButton().click();

        this.redirectWait(sourceUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("#/sponsor/tribute/" + tribute.getId().toString()));
    }

}
