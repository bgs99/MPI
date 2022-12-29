package ru.itmo.hungergames.selenium;

import org.hamcrest.CoreMatchers;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.response.JwtResponse;
import ru.itmo.hungergames.selenium.pages.LoginPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.Utils;
import ru.itmo.hungergames.service.SecurityService;

import java.util.HashSet;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "server.port=42322", webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@SeleniumTest
public class LoginPageTests {
    @MockBean
    SecurityService securityService;

    @Autowired
    private WebDriver driver;

    private LoginPage loginPage;
    private String loginPageUrl;

    @BeforeEach
    public void setUp() {
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPageUrl = driver.getCurrentUrl();
    }

    @Test
    public void RedirectToRegister() {
        loginPage.getSponsorRegisterButton().click();

        Utils.redirectWait(driver, loginPageUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("/register"));
    }

    @Test
    public void RedirectToCapitolAuth() {
        loginPage.getCapitolAuthLink().click();

        Utils.redirectWait(driver, loginPageUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("/capitol/auth"));
    }

    @Transactional
    @Test
    public void LoginSponsor() {
        String username = "username";
        String password = "password";
        String name = "name";

        Mockito
                .doReturn(new JwtResponse(UUID.randomUUID(), "", username, new HashSet<>(), name))
                .when(securityService)
                .authenticateUser(new User(username, password));

        loginPage.getSponsorLoginInput().sendKeys(username);
        loginPage.getSponsorPasswordInput().sendKeys(password);

        loginPage.getSponsorLoginButton().click();

        Utils.redirectWait(driver, loginPageUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("/sponsor"));
    }
}

