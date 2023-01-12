package ru.itmo.hungergames.selenium.unit;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.AuthenticationException;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.response.JwtResponse;
import ru.itmo.hungergames.selenium.pages.ModeratorLoginPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.SecurityService;

import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SeleniumTest
public class ModeratorLoginPageTests extends SeleniumTestBase {
    @MockBean
    SecurityService securityService;

    private ModeratorLoginPage page;
    private String sourceUrl;

    @BeforeEach
    public void setUp() {
        this.get("/moderator/login");
        this.page = PageFactory.initElements(driver, ModeratorLoginPage.class);

        page.getLoginInput().clear();
        page.getPasswordInput().clear();

        sourceUrl = driver.getCurrentUrl();
    }


    @Test
    public void LoginSuccess() {
        String username = "username";
        String password = "password";
        String name = "name";

        doReturn(new JwtResponse(UUID.randomUUID(), "", username, Set.of(UserRole.MODERATOR.toString()), name))
                .when(securityService)
                .authenticateUser(new User(username, password));

        page.getLoginInput().sendKeys(username);
        page.getPasswordInput().sendKeys(password);

        page.getLoginButton().click();

        this.assertRedirect(sourceUrl, "/moderator");
    }

    @Test
    public void LoginSponsorFailure() {
        String username = "username";
        String password = "password";

        class MyAuthException extends AuthenticationException {
            public MyAuthException() {
                super("test");
            }
        }

        doThrow(new MyAuthException())
                .when(securityService)
                .authenticateUser(new User(username, password));

        page.getLoginInput().sendKeys(username);
        page.getPasswordInput().sendKeys(password);

        page.getLoginButton().click();

        this.assertNoRedirect(sourceUrl);

        assertThat(page.getLoginError().getText(), CoreMatchers.equalTo("Неправильный логин или пароль"));
    }
}

