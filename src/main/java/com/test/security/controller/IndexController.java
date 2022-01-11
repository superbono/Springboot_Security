package com.test.security.controller;

import com.test.security.model.User;
import com.test.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {

    @Autowired
    private UserRepository repository;

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
    @GetMapping("/loginForm")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public @ResponseBody String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        repository.save(user);
        return "join";
    }
}