package ru.itmo.hungergames.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.itmo.hungergames.exception.UserExistsException;
import ru.itmo.hungergames.model.entity.User;
import ru.itmo.hungergames.model.entity.UserRole;
import ru.itmo.hungergames.repository.UserRepository;

@Component
public class SecurityUtil {
    private final UserRepository userRepository;

    @Autowired
    public SecurityUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateBeforeSigningUp(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new UserExistsException(user);
    }

    public User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Long getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
    }

    public UserRole getAuthenticatedUserRole() {
        return getAuthenticatedUser().getUserRoles().iterator().next();
    }
}
