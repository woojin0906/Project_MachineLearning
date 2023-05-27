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

    // 이미지/파일을 업로드하는 진짜 경로를 application properties에 uploadPath로 저장해놨는데 그걸 가져오게 하기 위함
    @Value(value = "${uploadPath}")
    private String uploadPath;  // 해당 경로를 사용할 수 있음

    @Override
    // registry를 등록해서 쓸 수 있게 함
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
         registry.addResourceHandler("/images/**") // 접근을 이 주소로 하게 함
                 .addResourceLocations(uploadPath); // 실제로는 여기인데 (위에 처럼함)
    }
}
