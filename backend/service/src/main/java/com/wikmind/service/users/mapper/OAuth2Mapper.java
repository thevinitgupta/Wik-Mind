package com.wikmind.service.users.mapper;

import com.wikmind.service.users.entity.external.ExternalUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2Mapper {
    String getProvider();

    ExternalUser map(OAuth2User oAuth2User);
}
