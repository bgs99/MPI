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

public class Utils {
    public static void redirectWait(WebDriver driver, String originalUrl) {
        redirectWait(driver, originalUrl, Duration.ofSeconds(1));
    }

    public static void redirectWait(WebDriver driver, String originalUrl, Duration timeout) {
        new WebDriverWait(driver, timeout).until(
                ExpectedConditions.not(
                        ExpectedConditions.urlToBe(originalUrl)));
    }

    public static void authenticate(WebDriver driver, User user, UserRole role, JwtFilter jwtFilter, SecurityUtil securityUtil) {
        driver.get("http://localhost:42322/"); // To get to the localStorage for the site
        LocalStorage local = ((WebStorage) driver).getLocalStorage();
        ObjectMapper mapper = new ObjectMapper();
        var node = mapper.createObjectNode();
        node.put("id", user.getId().toString());
        node.put("role", role.asInt());
        node.put("token", "");
        node.put("name", user.getName());
        local.setItem("auth", node.toString());

        if (jwtFilter != null) {
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
                }).when(jwtFilter).doFilterInternal(any(), any(), any());
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (securityUtil != null) {
            doReturn(role).when(securityUtil).getAuthenticatedUserRole();
            doReturn(user.getId()).when(securityUtil).getAuthenticatedUserId();
            doReturn(user).when(securityUtil).getAuthenticatedUser();
        }
    }
}
