package com.project.machinlearning.config;
// 인증되지 않은 사용자 요청 시 에러
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

public class CustomEntryPoint implements AuthenticationEntryPoint {

    @Override
    // 인증되지 않은 사용자가 리소스를 요청할 경우, Unauthorized 에러 발생하고 나머지는 로그인 페이지로 리다이렉트.
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

    }
}
