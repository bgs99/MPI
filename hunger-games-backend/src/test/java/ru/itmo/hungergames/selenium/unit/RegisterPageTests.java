package ru.itmo.hungergames.selenium.unit;

import com.paulhammant.ngwebdriver.NgWebDriver;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.AuthenticationException;
import ru.itmo.hungergames.exception.UserExistsException;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.selenium.pages.RegisterPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.SecurityService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doNothing;

@SeleniumTest
public class RegisterPageTests extends SeleniumTestBase {
    @MockBean
    SecurityService securityService;

    private RegisterPage registerPage;

    private String registerPageUrl;

    @BeforeEach
    public void setUp() {
        this.get("/register");
        registerPage = PageFactory.initElements(driver, RegisterPage.class);

        NgWebDriver ngDriver = new NgWebDriver((FirefoxDriver)driver);
        ngDriver.waitForAngularRequestsToFinish();

        registerPageUrl = driver.getCurrentUrl();
    }

    @Test
    public void registerSponsorSuccess() {
        String username = "username";
        String password = "password";
        String name = "name";

        doNothing()
                .when(securityService)
                .createSponsor(new Sponsor(username, password, name, null));

        registerPage.getSponsorNameInput().sendKeys(name);
        registerPage.getSponsorLoginInput().sendKeys(username);
        registerPage.getSponsorPasswordInput().sendKeys(password);
        registerPage.getSponsorPassword2Input().sendKeys(password);

        registerPage.getSponsorRegisterButton().click();

        this.redirectWait(registerPageUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("#/login"));
    }

    @Test
    public void registrationFailure() {
        String username = "username";
        String password = "password";
        String name = "name";

        Mockito.doThrow(new UserExistsException(new Sponsor(username, password, name, null)))
                .when(securityService)
                .createSponsor(new Sponsor(username, password, name, null));

        registerPage.getSponsorNameInput().sendKeys(name);
        registerPage.getSponsorLoginInput().sendKeys(username);
        registerPage.getSponsorPasswordInput().sendKeys(password);
        registerPage.getSponsorPassword2Input().sendKeys(password);

        registerPage.getSponsorRegisterButton().click();

        Assertions.assertThrows(Exception.class, () -> this.redirectWait(registerPageUrl));

        assertThat(registerPage.getRegisterError().getText(), CoreMatchers.equalTo("Пользователь с таким логином уже существует"));
    }

    @Test
    void registerFailureUnknownError() {
        String username = "username";
        String password = "password";
        String name = "name";

        class MyAuthException extends AuthenticationException {
            public MyAuthException() {
                super("test");
            }
        }

        Mockito.doThrow(new MyAuthException())
                .when(securityService)
                .createSponsor(new Sponsor(username, password, name, null));

        registerPage.getSponsorNameInput().sendKeys(name);
        registerPage.getSponsorLoginInput().sendKeys(username);
        registerPage.getSponsorPasswordInput().sendKeys(password);
        registerPage.getSponsorPassword2Input().sendKeys(password);

        registerPage.getSponsorRegisterButton().click();

        Assertions.assertThrows(Exception.class, () -> this.redirectWait(registerPageUrl));

        assertThat(registerPage.getRegisterError().getText(), CoreMatchers.equalTo("Неожиданная ошибка. Попробуйте повторить позже"));
    }

}
