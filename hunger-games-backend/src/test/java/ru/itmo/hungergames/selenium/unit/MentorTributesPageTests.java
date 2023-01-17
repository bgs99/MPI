package ru.itmo.hungergames.selenium.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.selenium.pages.MentorTributesPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.MentorService;
import ru.itmo.hungergames.service.TributeService;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.doReturn;

@SeleniumTest
public class MentorTributesPageTests extends SeleniumTestBase {
    @MockBean
    private TributeService tributeService;

    @MockBean
    private MentorService mentorService;

    private final Mentor mentor = Mentor.builder()
            .id(new UUID(42, 42))
            .username("mentor")
            .name("mentor")
            .userRoles(Set.of(UserRole.MENTOR))
            .build();

    private final Tribute tribute1 = Tribute.builder()
            .id(new UUID(42, 42))
            .name("test")
            .mentor(mentor)
            .build();

    private final Tribute tribute2 = Tribute.builder()
            .id(new UUID(42, 43))
            .name("test 2")
            .mentor(mentor)
            .build();

    private final List<Tribute> tributeList = List.of(tribute1, tribute2);

    private List<MentorTributesPage.TributeRow> tributeRows;

    @BeforeEach
    public void setUp() {
        this.authenticate(this.mentor);

        doReturn(this.tributeList.stream().map(TributeResponse::new).collect(Collectors.toList())).when(this.mentorService).getAllTributes();

        final var page = this.getInit("/mentor/tributes", MentorTributesPage.class);

        this.tributeRows = page.getTributeRows();
    }

    @Test
    public void listTributes() {
        final var expectedNames = this.tributeList.stream().map(Tribute::getName).collect(Collectors.toList());
        final var actualNames = this.tributeRows.stream().map(MentorTributesPage.TributeRow::getName).collect(Collectors.toList());

        Assertions.assertEquals(expectedNames, actualNames);
    }

    @Test
    public void redirectTest() {
        doReturn(new TributeResponse(this.tribute2)).when(this.tributeService).getTributeById(this.tribute2.getId());

        this.assertRedirects(() -> this.tributeRows.get(1).getSelectButton().click(), "/mentor/tribute/" + this.tribute2.getId() + "/resources");
    }
}
