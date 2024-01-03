package com.mowski.gazetteLoader.repository;

import com.mowski.gazetteLoader.model.NameGazetteRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NameGazetteRecordRepository extends JpaRepository<NameGazetteRecord, Long> {
    boolean existsByIssueDate(String issueDate);
}
