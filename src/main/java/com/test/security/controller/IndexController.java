package com.test.security.controller;

import com.test.security.config.auth.PrincipalDetails;
import com.test.security.model.User;
import com.test.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder pwdEncode;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("/test/login =====================");
        PrincipalDetails details = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication - "+ details.getUser());
        return "세션정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(Authentication authentication) {
        System.out.println("/test/oauth/login =====================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication - "+ oAuth2User.getAttributes());
        return "OAuth 세션정보 확인하기";
    }

    @GetMapping("/test/oauth2/login")
    public @ResponseBody String testOauthLogin2(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("/test/oauth2/login =====================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication - "+ oAuth2User.getAttributes());
        System.out.println("oauth2User" + oauth.getAttributes());
        return "OAuth 세션정보 확인하기";
    }

    @GetMapping({"","/"})
    public String index() {
        return "index";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // SecurityConfig 파일을 생성하면 기존 Security 인풋창이 작동안함.
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = pwdEncode.encode(rawPassword);
        user.setPassword(encPassword);
        repository.save(user); //이렇게 회원가입을 할 경우, 1. 비밀번호 암호화가 안되어있어서 그대로 노출/ 2. 패스워드가 암호화가 안되어있어 시큐리티 접근이 안된다.
        return "redirect:/login";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/priv")
    public @ResponseBody String priv() {
        return "어드민정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")

    @GetMapping("/info")
    public @ResponseBody String info () {
        return "유저정보";
    }
}