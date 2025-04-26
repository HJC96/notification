package com.example.core.repository.jpa;

import com.example.core.domain.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTemplateJpaRepository extends JpaRepository<EmailTemplate, String> {
    Optional<EmailTemplate> findByTemplateKey(String templateKey);
}
