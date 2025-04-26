package com.example.api.service.impl;

import com.example.api.service.EmailTemplateService;
import com.example.core.domain.EmailTemplate;
import com.example.core.repository.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {
    private final EmailTemplateRepository emailTemplateRepository;
    private final RedisTemplate<String, String> stringRedisTemplate;

    public String getTemplate(String templateKey) {
        String cachedTemplate = stringRedisTemplate.opsForValue().get(templateKey);
        if (cachedTemplate != null) {
            return cachedTemplate;
        }

        EmailTemplate template = emailTemplateRepository.findByTemplateKey(templateKey)
                .orElseThrow(() -> new IllegalArgumentException("이메일 템플릿을 찾을 수 없습니다: " + templateKey));

        stringRedisTemplate.opsForValue().set(templateKey, template.getTemplateContent(), 30, TimeUnit.MINUTES);
        return template.getTemplateContent();
    }
}
