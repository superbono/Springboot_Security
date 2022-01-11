package com.test.security.config;

import com.test.security.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// SecurityConfig를 메모리에 올리기 위해 필요한 어노테이션
@Configuration
/*
* 시큐리티를 활성화 시키기 위해 필요한 어노테이션
* SecurityConfig 활성화 시키면 스프링시큐리티필터(여기서는 SecurityConfig를 말함)가 스프링필터체인에 등록이 된다.
*/
@EnableWebSecurity
// secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)

/*
*  1.코드받기 (인증) 2. 엑세스토큰 받기 (권한생성) 3. 사용자프로필정보 가져오기 4. 정보를 토대로 회원가입 자동진행
*  4-2 (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집주소), 백화점몰 -> (vip등급, 일반등급)
*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService userService;

    // 시큐리티 암호화 Bean으로 등록
    // Bean으로 등록하게 되면 해당 메서드의 리턴되는 오브젝트를 Ioc로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

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
                .loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .defaultSuccessUrl("/")
            .and()
                .oauth2Login()
                .loginPage("/login") // 구글 로그인이 완료된 뒤의 후처리가 필요함. Tip. 로그인이 완료되면 (엑세스토큰 + 사용자프로필정보를 바로 받는다)
                .userInfoEndpoint()
                .userService(userService);

    }
}
