package com.test.security.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 로그인 후에 후처리가 필요함, loadUser에서 후처리가 진행됨
    // 구글로부터 받은 userRequest 데이터에 대한 후처리를 진행함.
    @Override
    // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인진행 -> code를 리턴 (OAuth-Client 라이브러리에 담긴다) -> AccessToken 요청
    // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필을 받음
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //getClientRegistration에서는 사용하고 있는 컴퓨터서버에 저장되어 있는 내용을 볼 수 있다.
        System.out.println("userRequest" + userRequest.getClientRegistration());
        // google에서 받아온 token value를 확인 할 수 있다.
        System.out.println("userRequest" + userRequest.getAccessToken().getTokenValue());
        //
        System.out.println("userRequest" + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        return super.loadUser(userRequest);
    }
}
