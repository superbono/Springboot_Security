package com.test.security.config.oauth.Provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;
    private Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
    private Map<String, Object> profile = (Map<String, Object>) account.get("profile");

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {


        if (profile == null) {
            return null;
        }

        return (String) profile.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) account.get("email");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

}