package com.project.machinlearning.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 *    이미지 업로드 파일 경로
 *
 *   @version          1.00 / 2023.05.27
 *   @author           전우진
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value(value = "${uploadPath}")
    private String uploadPath;

    @Override
    // registry를 등록해서 쓸 수 있게 함
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
         registry.addResourceHandler("/images/**") // 접근은 여기
                 .addResourceLocations(uploadPath); // 실제 경로는 여기
    }
}
