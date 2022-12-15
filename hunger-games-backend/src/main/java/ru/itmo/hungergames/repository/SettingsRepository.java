package ru.itmo.hungergames.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.hungergames.model.entity.user.Settings;

import java.util.UUID;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, UUID> {
}
