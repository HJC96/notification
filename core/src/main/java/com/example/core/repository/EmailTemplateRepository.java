package com.example.core.repository;

import com.example.core.domain.EmailTemplate;

import java.util.Optional;


public interface EmailTemplateRepository {
    Optional<EmailTemplate> findByTemplateKey(String templateKey);
}
