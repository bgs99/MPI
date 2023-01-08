package ru.itmo.hungergames.selenium.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.security.JwtFilter;
import ru.itmo.hungergames.util.SecurityUtil;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public abstract class SeleniumTestBase {
    @Autowired
    protected WebDriver driver;

    @LocalServerPort
    private int port;

    @SpyBean
    private JwtFilter jwtFilter;

    @SpyBean
    private SecurityUtil securityUtil;

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
        LocalStorage local = ((WebStorage) driver).getLocalStorage();
        ObjectMapper mapper = new ObjectMapper();
        var node = mapper.createObjectNode();
        node.put("id", user.getId().toString());
        node.put("role", role.asInt());
        node.put("token", "");
        node.put("name", user.getName());
        local.setItem("auth", node.toString());

        var authority = new SimpleGrantedAuthority(role.toString());

        try {
            doAnswer(invocation -> {
                var request = invocation.getArgument(0, HttpServletRequest.class);
                var response = invocation.getArgument(1, HttpServletResponse.class);
                var chain = invocation.getArgument(2, FilterChain.class);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, List.of(authority));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
                return null;
            }).when(this.jwtFilter).doFilterInternal(any(), any(), any());
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }

        doReturn(role).when(this.securityUtil).getAuthenticatedUserRole();
        doReturn(user.getId()).when(this.securityUtil).getAuthenticatedUserId();
        doReturn(user).when(this.securityUtil).getAuthenticatedUser();
    }
}
