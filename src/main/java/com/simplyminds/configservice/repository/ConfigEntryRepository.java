package com.simplyminds.configservice.repository;

import com.simplyminds.configservice.entity.ConfigEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigEntryRepository extends JpaRepository<ConfigEntry, Long> {
    List<ConfigEntry> findBySectionId(Long sectionId);

    @Query("SELECT ce FROM ConfigEntry ce WHERE ce.section.id = :sectionId AND ce.key = :key ORDER BY ce.version DESC")
    List<ConfigEntry> findLatestConfig(@Param("sectionId") Long sectionId, @Param("key") String key);
}
