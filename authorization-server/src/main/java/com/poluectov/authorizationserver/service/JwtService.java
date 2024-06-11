package com.poluectov.authorizationserver.service;

import com.poluectov.authorizationserver.model.TokenEntity;
import com.poluectov.authorizationserver.model.UserInfoDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;


@Service
public class JwtService {
    private String jwtSigningKey;

    private TokenService tokenService;

    private PasswordEncoder passwordEncoder;

    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_AUTHORITIES = "authorities";

    @Autowired
    public JwtService(
            @Value("${token.signing.key}") String jwtSigningKey,
            TokenService tokenService,
            PasswordEncoder passwordEncoder) {
        this.jwtSigningKey = jwtSigningKey;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Извлечение имени пользователя из токена
     *
     * @param token токен
     * @return имя пользователя
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get(CLAIM_EMAIL, String.class);
    }


    /**
     * Извлечение списка authorities из токена JWT.
     *
     * @param token токен
     * @return список authorities
     */
    public List<GrantedAuthority> extractAuthorities(String token) {
        List<String> authoritiesStrings = extractClaim(token, claims -> claims.get(CLAIM_AUTHORITIES, List.class));
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (authoritiesStrings != null && !authoritiesStrings.isEmpty()) {
            for (String authority : authoritiesStrings) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }
        return authorities;
    }

    public String generateToken(UserDetails userDetails) {
        List<String> authorities = new ArrayList<>(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof UserInfoDTO customUserDetails) {
            claims.put(CLAIM_EMAIL, customUserDetails.getEmail());
            claims.put(CLAIM_AUTHORITIES, authorities);
        }
        return generateToken(claims, userDetails.getUsername());
    }

    /**
     * Checks if a token is valid for a given user.
     *
     * @param  token     the token to be validated
     * @param  userDetails  the user details containing the username
     * @return          true if the token is valid, false otherwise
     */
    @Cacheable(value = "token", key = "#userDetails.username")
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);

        boolean isCryptoValid = (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);

        if (!isCryptoValid){
            return false;
        }

        List<TokenEntity> tokenEntities = tokenService.findAllByUsername(userName);

        boolean isSaved = false;
        for (TokenEntity tokenEntity : tokenEntities){
            if (passwordEncoder.matches(token, tokenEntity.getAuthenticationToken())) {
                isSaved = true;
                break;
            }
        }

        return isSaved;
    }

    /**
     * Извлечение данных из токена
     *
     * @param token           токен
     * @param claimsResolvers функция извлечения данных
     * @param <T>             тип данных
     * @return данные
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Генерация токена
     *
     * @param extraClaims дополнительные данные
     * @return токен
     */

    private String generateToken(Map<String, Object> extraClaims, String subject) {
        String token = Jwts.builder().setClaims(extraClaims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();

        return token;
    }

    /**
     * Проверка токена на просроченность
     *
     * @param token токен
     * @return true, если токен просрочен
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлечение даты истечения токена
     *
     * @param token токен
     * @return дата истечения
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлечение всех данных из токена
     *
     * @param token токен
     * @return данные
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token)
                .getBody();
    }

    /**
     * Получение ключа для подписи токена
     *
     * @return ключ
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
