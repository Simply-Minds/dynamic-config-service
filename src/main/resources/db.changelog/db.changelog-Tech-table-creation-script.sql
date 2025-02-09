--liquibase formatted sql
--changeset Abhishek :1
-- Tech: Create Basic Database Schema config service

-- ==========================
-- Database: dynamic_config_db
-- Purpose: To store dynamic configurations with versioning and disable features
-- ==========================

-- ==========================
-- Table: config_section
-- Purpose: Stores different configurable sections (e.g., Menu, Products, Suppliers)
-- ==========================
CREATE TABLE config_section (
    id SERIAL PRIMARY KEY,               -- Unique ID for the section
    name VARCHAR(100) UNIQUE NOT NULL,   -- Unique section name (e.g., 'menu', 'product_config')
    description TEXT,                    -- Optional description for the section
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Record creation timestamp
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Last update timestamp
);

-- ==========================
-- Table: config_entry
-- Purpose: Stores JSON configuration for each section with versioning and disable feature
-- ==========================
CREATE TABLE config_entry (
    id SERIAL PRIMARY KEY,                 -- Unique ID for the config entry
    section_id INT NOT NULL,               -- Foreign key referencing the section
    key VARCHAR(255) NOT NULL,             -- Unique key within the section (e.g., 'menu_items')
    title VARCHAR(100) NOT NULL,           -- Title of the section or item
    type VARCHAR(50) NOT NULL,             -- Type of the element (e.g., group, item, collapse)
    value JSONB NOT NULL,                  -- Configuration data stored in JSON format
    version INT NOT NULL DEFAULT 1,        -- Version number for tracking changes
    is_active BOOLEAN DEFAULT FALSE,       -- Flag to indicate the currently used version
    is_visible BOOLEAN DEFAULT FALSE,
    is_editable BOOLEAN DEFAULT FALSE,
    can_edit JSON,                         -- Array of roles allowed to edit this section/item
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Record creation timestamp
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Last update timestamp
    FOREIGN KEY (section_id) REFERENCES config_section(id) ON DELETE CASCADE,
    UNIQUE (section_id, key, version)      -- Ensure unique versioning per key
);

-- ==========================
-- Table: config_history
-- Purpose: Tracks changes made to configurations for audit and rollback purposes
-- ==========================
CREATE TABLE config_history (
    id SERIAL PRIMARY KEY,                 -- Unique ID for the history entry
    config_id INT NOT NULL,                -- Foreign key referencing config_entry
    old_value JSONB NOT NULL,              -- JSON data before the update
    new_value JSONB NOT NULL,              -- JSON data after the update
    version INT NOT NULL,                  -- Version number for tracking changes
    is_editable BOOLEAN DEFAULT FALSE,
    can_edit JSON,                         -- Array of roles allowed to edit this section/item
    changed_by VARCHAR(100),               -- User who changed the configuration
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of the change
    FOREIGN KEY (config_id) REFERENCES config_entry(id) ON DELETE CASCADE
);