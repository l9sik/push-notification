package com.poluectov.authorizationserver.service;


import com.poluectov.authorizationserver.model.TokenEntity;
import com.poluectov.authorizationserver.model.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@Slf4j
public class TokenRegistrationService {

    JwtService jwtService;
    TokenService tokenService;
    private static BigInteger BILLION;


    @Autowired
    TokenRegistrationService(
            JwtService service,
            TokenService tokenService) {
        this.tokenService = tokenService;
        this.jwtService = service;
        BILLION = BigInteger.valueOf(999_999_999L);
    }

    public String generateToken(UserInfoDTO userInfo) {
        //generate verification code

        return is2FVerified(userInfo);
    }
    private String is2FVerified(UserInfoDTO userInfoDTO){
        //generate jwt
        String token = jwtService.generateToken(userInfoDTO);
        TokenEntity tokenEntity = TokenEntity.builder()
                .username(userInfoDTO.getUsername())
                .authenticationToken(token)
                .build();

        tokenService.save(tokenEntity);

        //send response to db

        return token;
    }
}
