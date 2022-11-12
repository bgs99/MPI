package ru.itmo.hungergames.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itmo.hungergames.model.entity.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    @Value("${hunger-games.jwt.secret}")
    private String secret;
    @Value("${hunger-games.jwt.expirationMs}")
    private String expirationMs;

    public String generateJwtToken(User user) {
        Date expirationDate;
        try {
            expirationDate = DateFormat.getDateInstance().parse(System.currentTimeMillis() + expirationMs);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return Jwts.builder()
                .setSubject((user.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
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
}
