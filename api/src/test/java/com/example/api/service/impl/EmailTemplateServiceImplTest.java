package com.example.api.service.impl;

import com.example.api.service.EmailTemplateService;
import com.example.core.domain.EmailTemplate;
import com.example.core.repository.EmailTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class EmailTemplateServiceImplTest {

    private EmailTemplateRepository emailTemplateRepository;
    private RedisTemplate<String, String> stringRedisTemplate;
    private ValueOperations<String, String> valueOperations;

    private EmailTemplateService emailTemplateService;

    @BeforeEach
    void setUp() {
        emailTemplateRepository = mock(EmailTemplateRepository.class);
        stringRedisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);

        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);

        emailTemplateService = new EmailTemplateServiceImpl(emailTemplateRepository, stringRedisTemplate);
    }

    @Test
    void 캐시에_존재하면_DB를_조회하지_않는다() {
        // given
        String templateKey = "email-template:default";
        String cachedTemplate = "<html><body>Cached Template</body></html>";

        given(valueOperations.get(templateKey)).willReturn(cachedTemplate);

        // when
        String result = emailTemplateService.getTemplate(templateKey);

        // then
        assertThat(result).isEqualTo(cachedTemplate);

        // DB는 조회하지 않아야 한다
        then(emailTemplateRepository).shouldHaveNoInteractions();
    }

    @Test
    void 캐시에_없으면_DB를_조회하고_캐시에_저장한다() {
        // given
        String templateKey = "email-template:default";
        String dbTemplateContent = "<html><body>DB Template</body></html>";

        given(valueOperations.get(templateKey)).willReturn(null); // 캐시 없음
        given(emailTemplateRepository.findByTemplateKey(templateKey))
                .willReturn(Optional.of(new EmailTemplate(templateKey, dbTemplateContent)));

        // when
        String result = emailTemplateService.getTemplate(templateKey);

        // then
        assertThat(result).isEqualTo(dbTemplateContent);

        // DB에서 조회했어야 하고
        then(emailTemplateRepository).should().findByTemplateKey(templateKey);

        // Redis에 캐시 저장했어야 한다
        then(valueOperations).should().set(templateKey, dbTemplateContent, 30, TimeUnit.MINUTES);
    }
}