package com.wikmind.service.users.entity.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
public class ExternalUser {

    private String provider;

    private String providerUserId;

    private String email;

    private String firstName;

    private String lastName;

    private String displayName;

    private String avatarUrl;

}
