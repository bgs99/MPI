package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.user.Settings;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.request.SettingsChangeRequest;
import ru.itmo.hungergames.model.response.SettingsResponse;
import ru.itmo.hungergames.repository.UserRepository;
import ru.itmo.hungergames.service.UserService;
import ru.itmo.hungergames.util.SecurityUtil;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           SecurityUtil securityUtil) {
        this.userRepository = userRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    @Transactional
    public void changeSettings(SettingsChangeRequest settingsChangeRequest) {
        User user = userRepository
                .findById(securityUtil.getAuthenticatedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no user with such ID"));
        userRepository.save(user.updateSettings(settingsChangeRequest));
    }

    @Override
    public SettingsResponse getSettings() {
        Settings settings = userRepository
                .findById(securityUtil.getAuthenticatedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no user with such ID"))
                .getSettings();
        return new SettingsResponse(settings);
    }
}
