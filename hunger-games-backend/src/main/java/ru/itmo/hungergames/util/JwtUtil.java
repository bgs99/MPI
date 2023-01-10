package ru.itmo.hungergames.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.security.UserDetailsServiceImpl;

import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    @Value("${hunger-games.jwt.secret}")
    private String secret;
    @Value("${hunger-games.jwt.expirationMs}")
    private String expirationMs;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtUtil(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String generateJwtToken(User user) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + Long.parseLong(expirationMs));

        return Jwts.builder()
                .setSubject((user.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(calendar.toInstant()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public UsernamePasswordAuthenticationToken createAuthenticationFromJwtToken(String token) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserNameFromJwtToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
