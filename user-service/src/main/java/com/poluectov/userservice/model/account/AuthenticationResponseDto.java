
package com.poluectov.userservice.model.account;

import com.poluectov.userservice.entity.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Builder
@Getter
@Setter
public class AuthenticationResponseDto {

    private Long userId;

    private String userName;

    private String userEmail;

    private String password;

    Collection<AuthorityResponseDto> authorities;

}