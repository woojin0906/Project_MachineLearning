package com.project.machinlearning.config;
// 언제나 쓸 수 있도록 빈 등록
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 *    AuditorAware로 반환
 *
 *   @version          1.00 / 2023.05.22
 *   @author           전우진
 */
@Configuration
@EnableJpaAuditing //변경 발생 시 자동으로 감지할 수 있게 함

public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl(); // 클래스를 생성해서 AuditorAware로 반환
    }

}
