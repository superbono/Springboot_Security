package com.test.security.config.auth;

import com.test.security.model.User;
import com.test.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl에서 login 요청이 오면 자동으로 UserDetailsService 타입으 Loc되어있는 loadUserByname이 호출됨
// 로그인 로직 작동순서 => view에서 action="/login"이 요청되면, UserDetailService가 등록된 클래스를 찾는다.
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    // UserDetails를 통해 유저정보를 리턴하게 되면
    // PrincipalDetail를 객체에 User정보를 담게 되면 Security Session안에 Authentication 내부에 들어가게 된다.(session(Authentication(userEntity));)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: "+username);
        User userEntity = repository.findByUsername(username);
        if(userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
