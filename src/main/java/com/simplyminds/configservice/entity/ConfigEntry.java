package com.simplyminds.configservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "config_entry", uniqueConstraints = @UniqueConstraint(columnNames = {"section_id", "key", "version"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private ConfigSection section;

    @Column(nullable = false, length = 255)
    private String key;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(columnDefinition = "JSON")
    private String value;

    @Column(nullable = false)
    private int version = 1;

    @Column(name = "is_active")
    private boolean isActive = false;

    @Column(name = "is_visible")
    private boolean isVisible = false;

    @Column(name = "is_editable")
    private boolean isEditable = false;

    @Column(columnDefinition = "JSON")
    private String canEdit;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
}
