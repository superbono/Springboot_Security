package com.test.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// SecurityConfig를 메모리에 올리기 위해 필요한 어노테이션
@Configuration
// 시큐리티를 활성화 시키기 위해 필요한 어노테이션
// SecurityConfig 활성화 시키면 스프링시큐리티필터(여기서는 SecurityConfig를 말함)가 스프링필터체인에 등록이 된다.
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // csrf는 Cross site Request forgery를 말하며, 정상적인 사용자가 의도치 않은 위조요청을 보내는 것을 말한다.
        http.csrf().disable();
        http.authorizeRequests()
                // 요청에 따른 옵션을 설정할 수 있다. 옵션/해당url에 따라 권한을 달리 줄 수 있다.
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                // 나머지 요청은 옵션없이 권한이 허용된다.
                .anyRequest().permitAll()
                .and()
                // 위에 설정한 antMatchers에 설정된 url로 접근시 설정한 페이지로 이동하도록 설정.
                .formLogin()
                // 로그인 페이지로 이동.
                .loginPage("/login");
    }
}
