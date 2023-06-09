package com.project.machinlearning.config;
import com.project.machinlearning.User.Role;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *    스프링부트 시큐리티 로그인 필터 Config
 *
 *   @version          1.00 / 2023.05.23
 *   @author           전우진
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.formLogin() // 로그인과 관련된 주소
                .loginPage("/user/login")  // 로그인 주소
                .defaultSuccessUrl("/api/diary/list/1") // 성공 시 이동할 주소
                .usernameParameter("nickName") // user이름을 nickName로 사용할 것이기 때문에 field이름을 적어줘야 함  -> username이라 적은 경우엔 안적어도 됨
                .passwordParameter("pw")// -> password라 적은 경우엔 안적어도 됨
                .failureUrl("/user/login/error") // 로그인 실패 시 이동할 페이지
                .successHandler((request, response, authentication) -> {
                    // 인증된 사용자 정보 가져오기
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                    // 사용자의 role 확인
                    boolean isAdmin = userDetails.getAuthorities().stream()
                            .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

                    // role이 ADMIN인 경우에만 특정 URL로 리디렉션
                    if (isAdmin) {
                        response.sendRedirect("/admin/list");
                    } else {
                        // 그 외의 경우 기본 successUrl로 리디렉션
                        response.sendRedirect("/api/diary/list/1");
                    }
                })
                .and()
                .logout()  // 로그아웃과 관련된 정보
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 로그아웃을 누를 때 처리할 내용
                .logoutSuccessUrl("/user/login");

        http.authorizeHttpRequests()  // 인증 여부 확인 -> 스프링 3.0 이하 버전은 authorizeRequests()로 설정
                // 스프링 3.0 이하 버전은 antMatchers(), mvcMatchers(), regexMatchers()으로 사용
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // 페이지 이동할 경우 default로 인증이 걸리도록 되어있기 때문에 추가
                .requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/resources/**", "/error", "/api/**", "/images/**", "/").hasRole("USER")  // 모든 사람에게 css 적용
                .requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/resources/**", "/error", "/api/**", "/images/**", "/").hasRole("ADMIN")
                .requestMatchers("/user/**").permitAll()// 아무나 페이지에 들어올 수 있고, member, item 밑에 있는 애들은 모두 permit 허용
                .requestMatchers("/admin/**").hasRole("ADMIN") // admin인 애들만 admin에 접속 가능
                .requestMatchers("/ban/**").hasRole("BAN") // ban인 애들만 ban에 접속 가능
                .anyRequest().authenticated(); // 인증 받기

        http.exceptionHandling()  // 권한이 없는 경우
                .accessDeniedPage("/user/ban")
                .authenticationEntryPoint(new CustomEntryPoint());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();// 단방향 암호화 객체 생성
    }
}

