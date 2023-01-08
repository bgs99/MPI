package ru.itmo.hungergames.selenium.unit;

import com.paulhammant.ngwebdriver.NgWebDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.user.Mentor;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.Tribute;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.selenium.pages.SponsorTributePage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.TributeService;

import java.util.UUID;

import static org.mockito.Mockito.doReturn;

@SeleniumTest
public class SponsorTributePageTests extends SeleniumTestBase {
    private SponsorTributePage page;

    @MockBean
    private TributeService tributeService;

    private final int district = 3;
    private Tribute tribute;

    @BeforeEach
    public void setUp() {
        this.page = PageFactory.initElements(driver, SponsorTributePage.class);

        Sponsor sponsor = Sponsor.builder()
                .id(new UUID(42, 42))
                .name("sponsor")
                .username("test")
                .password("test")
                .build();

        this.authenticate(sponsor, UserRole.SPONSOR);


        var mentor = Mentor.builder()
                .id(new UUID(42, 41))
                .district(district)
                .build();
        this.tribute = Tribute.builder()
                .id(new UUID(42, 42))
                .name("test")
                .mentor(mentor)
                .avatarUri("https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/Orange_tabby_cat_sitting_on_fallen_leaves-Hisashi-01A.jpg/800px-Orange_tabby_cat_sitting_on_fallen_leaves-Hisashi-01A.jpg")
                .build();

        doReturn(new TributeResponse(tribute)).when(this.tributeService).getTributeById(tribute.getId());

        this.get("/sponsor/tribute/" + tribute.getId());

        NgWebDriver ngDriver = new NgWebDriver((FirefoxDriver)driver);
        ngDriver.waitForAngularRequestsToFinish();
    }

    @Test
    public void correctTitle() {
        Assertions.assertEquals("Трибут " + this.tribute.getName(), this.page.tributeTitle.getText());
    }

    @Test
    public void correctDistrict() {
        Assertions.assertEquals("Дистрикт " + this.district, this.page.tributeDistrict.getText());
    }

    @Test
    public void correctAvatar() {
        Assertions.assertEquals(this.tribute.getAvatarUri(), this.page.avatar.getAttribute("src"));
    }
}
