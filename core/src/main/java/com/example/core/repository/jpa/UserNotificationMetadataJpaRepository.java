package com.example.core.repository.jpa;

import com.example.core.domain.UserNotificationMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationMetadataJpaRepository extends JpaRepository<UserNotificationMetadata, Long> {
}
