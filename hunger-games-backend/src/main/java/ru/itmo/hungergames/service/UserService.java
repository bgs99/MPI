package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.request.SettingsChangeRequest;
import ru.itmo.hungergames.model.response.SettingsResponse;

public interface UserService {
    void changeSettings(SettingsChangeRequest settingsChangeRequest);

    SettingsResponse getSettings();
}
