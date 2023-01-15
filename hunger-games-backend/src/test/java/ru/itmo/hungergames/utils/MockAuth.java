package ru.itmo.hungergames.utils;

import static org.mockito.Mockito.doReturn;

import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.util.JwtUtil;
import ru.itmo.hungergames.util.SecurityUtil;

@Component
public class MockAuth {
    @SpyBean
    private SecurityUtil securityUtil;

    @SpyBean
    private JwtUtil jwtUtil;

    public void authenticate(User user) {
        final var role = user.getUserRoles().stream().findFirst().orElseThrow();

        doReturn(true).when(this.jwtUtil).validateJwtToken("test");
        doReturn(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()))
                .when(this.jwtUtil)
                .createAuthenticationFromJwtToken("test");

        doReturn(role).when(this.securityUtil).getAuthenticatedUserRole();
        doReturn(user.getId()).when(this.securityUtil).getAuthenticatedUserId();
        doReturn(user).when(this.securityUtil).getAuthenticatedUser();
    }
}
