package com.example.core.repository.impl;

import com.example.core.domain.UserNotificationMetadata;
import com.example.core.repository.UserNotificationMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UserNotificationMetadataRepositoryImpl implements UserNotificationMetadataRepository {

    private final JpaRepository<UserNotificationMetadata, Long> jpaRepository;

    @Override
    public UserNotificationMetadata save(UserNotificationMetadata metadata) {
        return jpaRepository.save(metadata);
    }

    @Override
    public Optional<UserNotificationMetadata> findById(Long userId) {
        return jpaRepository.findById(userId);
    }

    @Override
    public void deleteById(Long userId) {
        jpaRepository.deleteById(userId);
    }
}
