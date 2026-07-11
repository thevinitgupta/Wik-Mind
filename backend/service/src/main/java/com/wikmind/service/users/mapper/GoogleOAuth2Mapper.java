package com.wikmind.service.users.mapper;

import com.wikmind.service.users.entity.external.ExternalUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component(GoogleOAuth2Mapper.PROVIDER_NAME)
public class GoogleOAuth2Mapper implements OAuth2Mapper{

    public static final String PROVIDER_NAME = "google";

    @Override
    public String getProvider() {
        return PROVIDER_NAME;
    }

    @Override
    public ExternalUser map(OAuth2User oAuth2User) {
        return ExternalUser.builder()
                .provider(PROVIDER_NAME)
                .providerUserId(oAuth2User.getAttribute("sub"))
                .displayName(oAuth2User.getAttribute("name"))
                .email(oAuth2User.getAttribute("email"))
                .firstName(oAuth2User.getAttribute("given_name"))
                .lastName(oAuth2User.getAttribute("family_name"))
                .avatarUrl(oAuth2User.getAttribute("picture"))
                .build();
    }
}
