package com.example.demo.config;

import com.example.demo.entity.SystemSettings;
import com.example.demo.repository.SystemSettingsRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SystemSettingsInitializer {

    private final SystemSettingsRepository repository;

    @PostConstruct
    @Transactional
    public void init() {
        repository.findById(1L).ifPresentOrElse(
            existing -> System.out.println("✅ SystemSettings already exists."),
            () -> {
                SystemSettings settings = new SystemSettings();
                settings.setId(1L); // explicitly set the ID
                settings.setAllowRegistrations(true);  // enable registrations by default
                settings.setFreezeEdits(false);        // optional feature
                repository.save(settings);
                System.out.println("🆕 SystemSettings initialized.");
            }
        );
    }
}
