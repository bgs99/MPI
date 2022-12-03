package ru.itmo.hungergames.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.itmo.hungergames.util.JwtUtil;

import java.util.Optional;

@Service
@Slf4j
public class WebSocketChannelInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public WebSocketChannelInterceptor(JwtUtil jwtUtil,
                                       UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            UsernamePasswordAuthenticationToken user = createAuthenticationFromJwtToken(parseJwt(accessor).get());
//            SecurityContextHolder.getContext().setAuthentication(user);
            accessor.setUser(user);
        }
        return message;
    }

    private Optional<String> parseJwt(StompHeaderAccessor accessor) {
        String headerAuth = accessor.getPasscode();

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return Optional.of(headerAuth.substring(7));
        }
        return Optional.empty();
    }

    private UsernamePasswordAuthenticationToken createAuthenticationFromJwtToken(String jwt) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.getUserNameFromJwtToken(jwt));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return authentication;
    }
}
