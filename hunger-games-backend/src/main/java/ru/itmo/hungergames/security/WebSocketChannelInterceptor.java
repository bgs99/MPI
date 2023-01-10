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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.itmo.hungergames.util.JwtUtil;

import java.util.Optional;

@Service
@Slf4j
public class WebSocketChannelInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;

    @Autowired
    public WebSocketChannelInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            UsernamePasswordAuthenticationToken user = this.jwtUtil.createAuthenticationFromJwtToken(parseJwt(accessor).orElseThrow());
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
}
