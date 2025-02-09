package com.simplyminds.configservice.repository;

import com.simplyminds.configservice.entity.ConfigHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigHistoryRepository extends JpaRepository<ConfigHistory, Long> {
    List<ConfigHistory> findByConfigEntryId(Long configId);
}