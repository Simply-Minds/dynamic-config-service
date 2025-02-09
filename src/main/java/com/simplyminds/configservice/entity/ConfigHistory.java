package com.simplyminds.configservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "config_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "config_id", nullable = false)
    private ConfigEntry configEntry;

    @Column(columnDefinition = "JSON")
    private String oldValue;

    @Column(columnDefinition = "JSON")
    private String newValue;

    private int version;

    @Column(name = "is_editable")
    private boolean isEditable;

    @Column(columnDefinition = "JSON")
    private String canEdit;

    @Column(length = 100)
    private String changedBy;

    @Column(name = "changed_at", updatable = false)
    @CreationTimestamp
    private Timestamp changedAt;
}
