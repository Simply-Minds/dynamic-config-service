package com.simplyminds.configservice.repository;

import com.simplyminds.configservice.entity.ConfigSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigSectionRepository extends JpaRepository<ConfigSection, Long> {
    Optional<ConfigSection> findByName(String name);
}
