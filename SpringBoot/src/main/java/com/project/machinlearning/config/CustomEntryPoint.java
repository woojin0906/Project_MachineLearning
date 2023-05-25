package com.project.machinlearning.config;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 *    인증되지 않은 사용자 요청 시 에러나도록 엔트리 포인트 설정
 *
 *   @version          1.00 / 2023.05.22
 *   @author           전우진
 */
public class CustomEntryPoint implements AuthenticationEntryPoint {

    @Override
    // 인증되지 않은 사용자가 리소스를 요청할 경우, Unauthorized 에러 발생하고 나머지는 로그인 페이지로 리다이렉트.
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

    }
}
