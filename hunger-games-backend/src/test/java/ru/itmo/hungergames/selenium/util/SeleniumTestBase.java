package ru.itmo.hungergames.selenium.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.selenium.pages.MockPaymentPage;
import ru.itmo.hungergames.util.JwtUtil;
import ru.itmo.hungergames.util.SecurityUtil;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public abstract class SeleniumTestBase {
    @Autowired
    protected WebDriver driver;

    @LocalServerPort
    private int port;

    @SpyBean
    private SecurityUtil securityUtil;

    @SpyBean
    private JwtUtil jwtUtil;

    protected void waitForAngularRequests() {
        NgWebDriver ngDriver = new NgWebDriver((FirefoxDriver)driver);
        ngDriver.waitForAngularRequestsToFinish();
    }

    protected void closePaymentWindows(String sourceWindowHandle) {
        for (var windowHandle : this.driver.getWindowHandles()) {
            if (windowHandle.equals(sourceWindowHandle)) {
                continue;
            }
            this.driver.switchTo().window(windowHandle);
            var paymentPage = PageFactory.initElements(driver, MockPaymentPage.class);
            paymentPage.deny();
        }
        new WebDriverWait(this.driver, Duration.ofSeconds(1)).until(
                ExpectedConditions.numberOfWindowsToBe(1)
        );
        this.driver.switchTo().window(sourceWindowHandle);
    }

    protected void denyPayment(String sourceWindowHandle) {
        this.switchToNewWindow(sourceWindowHandle);
        var paymentPage = PageFactory.initElements(driver, MockPaymentPage.class);
        paymentPage.deny();
        new WebDriverWait(this.driver, Duration.ofSeconds(1)).until(
                ExpectedConditions.numberOfWindowsToBe(1)
        );
        this.driver.switchTo().window(sourceWindowHandle);
    }

    protected void approvePayment(String sourceWindowHandle) {
        this.switchToNewWindow(sourceWindowHandle);
        var paymentPage = PageFactory.initElements(driver, MockPaymentPage.class);
        paymentPage.approve();
        new WebDriverWait(this.driver, Duration.ofSeconds(1)).until(
                ExpectedConditions.numberOfWindowsToBe(1)
        );
        this.driver.switchTo().window(sourceWindowHandle);
    }

    protected void switchToNewWindow(Set<String> oldWindows) {
        new WebDriverWait(this.driver, Duration.ofSeconds(1)).until(
                ExpectedConditions.numberOfWindowsToBe(oldWindows.size() + 1)
        );
        var handles = this.driver.getWindowHandles();
        handles.removeAll(oldWindows);

        var newWindow = handles.stream().findFirst().orElseThrow();
        this.driver.switchTo().window(newWindow);
    }

    protected void switchToNewWindow(String sourceWindowHandle) {
        new WebDriverWait(this.driver, Duration.ofSeconds(1)).until(
                ExpectedConditions.numberOfWindowsToBe(2)
        );
        var handles = this.driver.getWindowHandles();
        handles.remove(sourceWindowHandle);

        var newWindow = handles.stream().findFirst().orElseThrow();
        this.driver.switchTo().window(newWindow);
    }

    protected void assertRedirect(String sourceUrl, String destination) {
        this.redirectWait(sourceUrl);

        assertThat(driver.getCurrentUrl(), CoreMatchers.endsWith("#" + destination));
    }

    protected void assertNoRedirect(String sourceUrl) {
        Assertions.assertThrows(Exception.class, () -> this.redirectWait(sourceUrl));
    }

    protected void redirectWait(String originalUrl) {
        redirectWait(originalUrl, Duration.ofSeconds(1));
    }

    protected void redirectWait(String originalUrl, Duration timeout) {
        new WebDriverWait(this.driver, timeout).until(
                ExpectedConditions.not(
                        ExpectedConditions.urlToBe(originalUrl)));
    }

    protected void get(String relativeUrl) {
        this.driver.get(this.composeUrl(relativeUrl));
    }

    protected String composeUrl(String relativeUrl) {
        return String.format("localhost:%d/#%s", this.port, relativeUrl);
    }

    protected void authenticate(User user, UserRole role) {
        driver.get(this.composeUrl("/")); // To get to the localStorage for the site

        final var node = new ObjectMapper().createObjectNode();
        node.put("id", user.getId().toString());
        node.put("role", role.asInt());
        node.put("token", "test");
        node.put("name", user.getName());
        ((WebStorage) driver).getLocalStorage().setItem("auth", node.toString());

        final var authority = new SimpleGrantedAuthority(role.toString());

        doReturn(true).when(this.jwtUtil).validateJwtToken("test");
        doReturn(new UsernamePasswordAuthenticationToken(user, null, List.of(authority)))
                .when(this.jwtUtil)
                .createAuthenticationFromJwtToken("test");

        doReturn(role).when(this.securityUtil).getAuthenticatedUserRole();
        doReturn(user.getId()).when(this.securityUtil).getAuthenticatedUserId();
        doReturn(user).when(this.securityUtil).getAuthenticatedUser();
    }
}
