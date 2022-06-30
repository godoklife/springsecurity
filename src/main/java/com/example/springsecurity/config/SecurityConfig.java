package com.example.springsecurity.config;

import com.example.springsecurity.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration  // 현 클래스를 설정파일 클래스로 설정
// extends WebSecurityConfigurerAdapter : 스프링 시큐리티 설정 기능 제공
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/memberonly").hasRole("MEMBER")   // 해당 페이지의 접근 권한 지정
                .antMatchers("/adminonly").hasRole("Admin")     // 상동
                .antMatchers("/**").permitAll()          // 그 외 경로는 모든 권한의 접근 허용()
                .and()                                              // 그리고
                .formLogin()                                        // 로그인 명령어 입력 시작
                .loginPage("/member/login")                         // 로그인 페이지 경로 지정
                .loginProcessingUrl("/member/login-processing")     // 로그인 데이타를 받을 경로 지정
                .defaultSuccessUrl("/")                             // 로그인 성공시 이동 경로
                .failureUrl("/member/login/error")// 로그인 처리 중 예외 발생시
                .usernameParameter("memberid")                      // 계정 변수명 지정
                .passwordParameter("memberpassword")                // 비밀번호 변수명 지정
                .and()
                .logout()                                           // 로그아웃 명령어 입력 시작
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))  // 로그아웃 요청 받을 경로 지정
                .logoutSuccessUrl("/")                              // 로그아웃 후 이동할 경로 지정
                .invalidateHttpSession(true)                        // session invalidate
                .and()
                .csrf() // 사이트간 요청 위조 막는 메서드 -> 화이트리스트 지정(이하 URI만 사용자로부터 입력을 받을수 있음)
                .ignoringAntMatchers("/member/login-processing")    // 화이트리스트 지정
                .ignoringAntMatchers("/member/signup-processing")   // 상동
                .and()
                .exceptionHandling()                                // 예외 발생시 명령어 입력 시작
                .accessDeniedPage("/error");           // 예외 발생시 이동 페이지 지정 -> 타임리프와 연계


    }

    @Autowired MemberService memberService; // MemberService 공갈객체 자동선언


    @Autowired private PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
        // BCryptPasswordEncoder 메서드를 지닌 복호화 공갈객체 선언

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {  // 인증 객체 오버라이드
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
            // 1. UserDetailsService를 구현한 클래스 지정
            // 2. 복호화 객체를 통해 암호화된 비밀번호를 복호화 처리 후 검증
    }
}
