package com.example.core.repository.impl;

import com.example.core.domain.UserNotificationMetadata;
import com.example.core.repository.UserNotificationMetadataRepository;
import com.example.core.repository.jpa.UserNotificationMetadataJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserNotificationMetadataRepositoryImpl implements UserNotificationMetadataRepository {

    private final UserNotificationMetadataJpaRepository jpaRepository;

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
