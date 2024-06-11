package com.poluectov.authorizationserver.repository;

import com.poluectov.authorizationserver.model.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    List<TokenEntity> findAllByUsername(String username);

    void deleteAllByUsername(String username);

    Optional<TokenEntity> findByAuthenticationToken(String token);
}
