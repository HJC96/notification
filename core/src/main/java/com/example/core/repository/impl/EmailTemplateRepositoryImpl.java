package com.example.core.repository.impl;

import com.example.core.domain.EmailTemplate;
import com.example.core.repository.EmailTemplateRepository;
import com.example.core.repository.jpa.EmailTemplateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class EmailTemplateRepositoryImpl implements EmailTemplateRepository {
    private final EmailTemplateJpaRepository jpaRepository;


    @Override
    public Optional<EmailTemplate> findByTemplateKey(String templateKey) {
        return jpaRepository.findByTemplateKey(templateKey);
    }
}
