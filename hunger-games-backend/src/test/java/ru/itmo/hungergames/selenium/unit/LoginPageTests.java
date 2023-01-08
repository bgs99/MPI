package ru.itmo.hungergames.selenium.unit;

import org.hamcrest.CoreMatchers;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.openqa.selenium.support.PageFactory;

import org.springframework.security.core.AuthenticationException;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.response.JwtResponse;
import ru.itmo.hungergames.selenium.pages.LoginPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.SecurityService;

import java.util.HashSet;
import java.util.UUID;

@SeleniumTest
public class LoginPageTests extends SeleniumTestBase {
    @MockBean
    SecurityService securityService;

    private LoginPage loginPage;
    private String loginPageUrl;

    @BeforeEach
    public void setUp() {
        this.get("/");
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPageUrl = driver.getCurrentUrl();
    }

    @Test
    public void RedirectToRegister() {
        loginPage.getSponsorRegisterButton().click();

        this.redirectWait(loginPageUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("#/register"));
    }

    @Test
    public void RedirectToCapitolAuth() {
        loginPage.getCapitolAuthLink().click();

        this.redirectWait(loginPageUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("#/capitol/auth"));
    }

    @Test
    public void LoginSponsorSuccess() {
        String username = "username";
        String password = "password";
        String name = "name";

        doReturn(new JwtResponse(UUID.randomUUID(), "", username, new HashSet<>(), name))
                .when(securityService)
                .authenticateUser(new User(username, password));

        loginPage.getSponsorLoginInput().sendKeys(username);
        loginPage.getSponsorPasswordInput().sendKeys(password);

        loginPage.getSponsorLoginButton().click();

        this.redirectWait(loginPageUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("#/sponsor"));
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

        loginPage.getSponsorLoginInput().sendKeys(username);
        loginPage.getSponsorPasswordInput().sendKeys(password);

        loginPage.getSponsorLoginButton().click();

        Assertions.assertThrows(Exception.class, () -> this.redirectWait(loginPageUrl));

        assertThat(loginPage.getLoginError().getText(), CoreMatchers.equalTo("Неправильный логин или пароль"));
    }
}

