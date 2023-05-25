package com.project.machinlearning.config;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 *    현재 로그인한 사용자의 정보를 등록자와 수정자로 지정
 *
 *   @version          1.00 / 2023.05.22
 *   @author           전우진
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication(); // 인증 관련 정보를 가져와 변수 생성
        String userId ="";

        if(authentication != null) { // 유저가 존재하면
            userId = authentication.getName(); // 유저 아이디 가져오기
        }

        return Optional.of(userId); // 내용이 있으면 들고가고 없으면 빈 걸 들고감
    }
}
