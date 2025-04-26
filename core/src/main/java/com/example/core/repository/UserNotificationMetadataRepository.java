package com.example.core.repository;

import com.example.core.domain.UserNotificationMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserNotificationMetadataRepository extends JpaRepository<UserNotificationMetadata, Long> {
    @Override
    Optional<UserNotificationMetadata> findById(Long aLong);
}