package com.simplyminds.configservice.controller;

import com.simplyminds.configservice.entity.ConfigEntry;
import com.simplyminds.configservice.entity.ConfigSection;
import com.simplyminds.configservice.service.ConfigService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/sections")
    public ResponseEntity<List<ConfigSection>> getAllSections() {
        return ResponseEntity.ok(configService.getAllSections());
    }

    @PostMapping("/sections")
    public ResponseEntity<ConfigSection> createSection(@Valid @RequestBody ConfigSection section) {
        ConfigSection createdSection = configService.createSection(section);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSection);
    }

    @PutMapping("/sections/{id}")
    public ResponseEntity<ConfigSection> updateSection(@PathVariable Long id, @Valid @RequestBody ConfigSection section) {
        return ResponseEntity.ok(configService.updateSection(id, section));
    }

    @DeleteMapping("/sections/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        configService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }

    // ===========================
    // CONFIG ENTRY ENDPOINTS
    // ===========================

    @GetMapping("/entries")
    public ResponseEntity<List<ConfigEntry>> getAllEntries() {
        return ResponseEntity.ok(configService.getAllEntries());
    }

    @GetMapping("/entries/{id}")
    public ResponseEntity<ConfigEntry> getEntryById(@PathVariable Long id) {
        return ResponseEntity.ok(configService.getEntryById(id));
    }

    @PostMapping("/entries")
    public ResponseEntity<ConfigEntry> createEntry(@Valid @RequestBody ConfigEntry entry) {
        ConfigEntry createdEntry = configService.createConfigEntry(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntry);
    }

    @PutMapping("/entries/{id}")
    public ResponseEntity<ConfigEntry> updateEntry(@PathVariable Long id, @Valid @RequestBody ConfigEntry entry) {
        return ResponseEntity.ok(configService.updateConfigEntry(id, entry));
    }

    @DeleteMapping("/entries/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        configService.deleteConfigEntry(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/entries/{id}/activate")
    public ResponseEntity<ConfigEntry> activateEntry(@PathVariable Long id) {
        return ResponseEntity.ok(configService.activateConfigEntry(id));
    }

    @GetMapping("/entries/latest")
    public ResponseEntity<ConfigEntry> getLatestEntry(
            @RequestParam Long sectionId,
            @RequestParam String key) {
        return ResponseEntity.ok(configService.getLatestConfig(sectionId, key));
    }
}
