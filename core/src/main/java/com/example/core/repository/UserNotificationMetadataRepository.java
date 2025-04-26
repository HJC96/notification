package com.example.core.repository;

import com.example.core.domain.UserNotificationMetadata;
import java.util.Optional;

public interface UserNotificationMetadataRepository {
    UserNotificationMetadata save(UserNotificationMetadata metadata);
    Optional<UserNotificationMetadata> findById(Long userId);
    void deleteById(Long userId);
}
