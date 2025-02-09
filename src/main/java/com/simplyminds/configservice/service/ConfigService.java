package com.simplyminds.configservice.service;

import com.simplyminds.configservice.entity.ConfigEntry;
import com.simplyminds.configservice.entity.ConfigHistory;
import com.simplyminds.configservice.entity.ConfigSection;
import com.simplyminds.configservice.exception.DuplicateResourceException;
import com.simplyminds.configservice.exception.ResourceNotFoundException;
import com.simplyminds.configservice.repository.ConfigEntryRepository;
import com.simplyminds.configservice.repository.ConfigHistoryRepository;
import com.simplyminds.configservice.repository.ConfigSectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigService {

    private final ConfigSectionRepository sectionRepository;

    private final ConfigEntryRepository entryRepository;

    private final ConfigHistoryRepository historyRepository;

    public ConfigService(ConfigSectionRepository sectionRepository,
                         ConfigEntryRepository entryRepository,
                         ConfigHistoryRepository historyRepository) {
        this.sectionRepository = sectionRepository;
        this.entryRepository = entryRepository;
        this.historyRepository = historyRepository;
    }

    // ===========================
    // SECTION OPERATIONS
    // ===========================

    public List<ConfigSection> getAllSections() {
        return sectionRepository.findAll();
    }

    public ConfigSection createSection(ConfigSection section) {
        if (sectionRepository.findByName(section.getName()).isPresent()) {
            throw new DuplicateResourceException("Section name already exists");
        }
        return sectionRepository.save(section);
    }

    public ConfigSection updateSection(Long id, ConfigSection updatedSection) {
        ConfigSection existingSection = sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        existingSection.setName(updatedSection.getName());
        existingSection.setDescription(updatedSection.getDescription());
        return sectionRepository.save(existingSection);
    }

    public void deleteSection(Long id) {
        ConfigSection section = sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
        sectionRepository.delete(section);
    }

    // ===========================
    // CONFIG ENTRY OPERATIONS
    // ===========================

    public List<ConfigEntry> getAllEntries() {
        return entryRepository.findAll();
    }

    public ConfigEntry getEntryById(Long id) {
        return entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Config entry not found"));
    }

    public ConfigEntry createConfigEntry(ConfigEntry entry) {
        if (entryRepository.findLatestConfig(entry.getSection().getId(), entry.getKey()).size() > 0) {
            throw new DuplicateResourceException("A configuration with this key already exists");
        }
        return entryRepository.save(entry);
    }

    public ConfigEntry updateConfigEntry(Long id, ConfigEntry updatedEntry) {
        ConfigEntry existingEntry = entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Config entry not found"));

        // Step 1: Save old value in history
        ConfigHistory history = new ConfigHistory();
        history.setConfigEntry(existingEntry);
        history.setOldValue(existingEntry.getValue());
        history.setNewValue(updatedEntry.getValue());
        history.setVersion(existingEntry.getVersion());
        history.setChangedBy("system_user"); // Replace with actual logged-in user
        historyRepository.save(history);

        // Step 2: Update config entry
        existingEntry.setTitle(updatedEntry.getTitle());
        existingEntry.setType(updatedEntry.getType());
        existingEntry.setValue(updatedEntry.getValue());
        existingEntry.setActive(updatedEntry.isActive());
        existingEntry.setVersion(existingEntry.getVersion() + 1); // Increment version

        return entryRepository.save(existingEntry);
    }

    public void deleteConfigEntry(Long id) {
        ConfigEntry entry = entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Config entry not found"));
        entryRepository.delete(entry);
    }

    public ConfigEntry activateConfigEntry(Long id) {
        ConfigEntry entry = entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Config entry not found"));

        entry.setActive(true);
        return entryRepository.save(entry);
    }

    public ConfigEntry getLatestConfig(Long sectionId, String key) {
        return entryRepository.findLatestConfig(sectionId, key)
                .stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Config not found"));
    }
}