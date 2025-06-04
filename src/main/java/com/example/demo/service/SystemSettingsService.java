package com.example.demo.service;

import com.example.demo.entity.SystemSettings;
import com.example.demo.repository.SystemSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemSettingsService {
    private final SystemSettingsRepository repo;

    /**
     * Always returns the single SystemSettings row.
     * Creates it if it doesn't exist yet.
     */
    @Transactional
    public SystemSettings getSettings() {
        return repo.findById(1L)
                   .orElseGet(this::createDefaultSettings);
    }

    /**
     * Creates default settings - this is called within the transaction
     */
    private SystemSettings createDefaultSettings() {
        SystemSettings settings = new SystemSettings();
        settings.setId(1L);
        settings.setAllowRegistrations(true);
        settings.setFreezeEdits(false);
        SystemSettings saved = repo.save(settings);
        System.out.println("🆕 Created default SystemSettings: allowRegistrations=" + saved.isAllowRegistrations());
        return saved;
    }

    @Transactional
    public SystemSettings updateSettings(SystemSettings updated) {
        SystemSettings existing = getSettings();
        existing.setAllowRegistrations(updated.isAllowRegistrations());
        existing.setFreezeEdits(updated.isFreezeEdits());
        return repo.save(existing);
    }

    /**
     * Returns true if user registrations are allowed.
     */
    @Transactional(readOnly = true)  
    public boolean canRegisterUsers() {
        return repo.findById(1L)
                  .map(SystemSettings::isAllowRegistrations)
                  .orElse(true); // Default to true if no settings exist
    }

    /**
     * Returns true if edits are frozen.
     */
    @Transactional(readOnly = true)
    public boolean isFreezeEditsEnabled() {
        return repo.findById(1L)
                  .map(SystemSettings::isFreezeEdits)
                  .orElse(false); // Default to false if no settings exist
    }
}