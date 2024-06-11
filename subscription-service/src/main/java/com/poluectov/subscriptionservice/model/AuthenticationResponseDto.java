
package com.poluectov.subscriptionservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponseDto {
    private Long userId;

    private String userName;

    private String userEmail;

    private String password;

    Collection<Authority> authorities;

}