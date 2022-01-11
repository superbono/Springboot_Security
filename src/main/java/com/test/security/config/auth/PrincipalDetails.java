package com.test.security.config.auth;

// 시큐리티가 login 주소 요청이 오면 낚아채서 로그인을 진행한다.
// 로그인 진행이 완료가 되면 시큐리티 session을 만들어 넣어준다. (Security ContextHolder)
// Security Sesson안에 오브젝트 => Authentication 타입 객체를 넣을 수 있다.
// Authentication안에 User정보가 있어야함 => User오브젝트타입 -> UserDetails 타입객체


// Security Session => Authentication의 객체안에 => UserDetails의 타입(User정보)이어야 한다.

import com.test.security.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PrincipalDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private User user;
    private Map<String, Object> attributes;

    // 일반 시큐리티 로그인시 사용
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth2.0 로그인시 사용
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collet = new ArrayList<GrantedAuthority>();
        collet.add(()->{ return user.getRole();});
        return collet;
    }
}
