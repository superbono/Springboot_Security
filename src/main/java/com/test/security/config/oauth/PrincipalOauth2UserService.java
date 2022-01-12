package com.test.security.config.oauth;

import com.test.security.config.auth.PrincipalDetails;
import com.test.security.config.oauth.Provider.FacebookInfo;
import com.test.security.config.oauth.Provider.GoogleUserInfo;
import com.test.security.config.oauth.Provider.NaverUserInfo;
import com.test.security.config.oauth.Provider.OAuth2UserInfo;
import com.test.security.model.User;
import com.test.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
// PrincipalOauth2UserService 종료시 @AuthenticationPrincipal 어노테이션이 생성된다.
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    // 로그인 후에 후처리가 필요함, loadUser에서 후처리가 진행됨
    // 구글로부터 받은 userRequest 데이터에 대한 후처리를 진행함.
    @Override
    // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인진행 -> code를 리턴 (OAuth-Client 라이브러리에 담긴다) -> AccessToken 요청
    // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필을 받음
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // google에서 받아온 token value를 확인 할 수 있다.
        //System.out.println("userRequest" + userRequest.getAccessToken().getTokenValue());
        //
        //System.out.println("userRequest" + super.loadUser(userRequest).getAttributes());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //getClientRegistration에서는 사용하고 있는 컴퓨터서버에 저장되어 있는 내용을 볼 수 있다.

        // OAuth2를 통해 자동로그인을 진행할경우 각각 다른 속성이름을 갖고 있으므로 DI를 해줄 인터페이스를 만들어 사용한다.
        OAuth2UserInfo oAuth2UserInfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인");
            oAuth2UserInfo = new FacebookInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
            System.out.println("NaverInfo"+oAuth2User);
        } else {
            System.out.println("지원하지 않는 OAuth2입니다.");
        }

//        String provider = userRequest.getClientRegistration().getRegistrationId();
//        String providerId = oAuth2User.getAttribute("sub");
//        String email = oAuth2User.getAttribute("email");
//        String username = provider+"_"+providerId;
//        String role = "ROLE_USER";

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String username = provider + "_" + providerId;
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null) {
            userEntity = User.builder()
                    .username(username)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("로그인을 한적이 있습니다. 회원가입없이 자동로그인을 진행합니다.");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
