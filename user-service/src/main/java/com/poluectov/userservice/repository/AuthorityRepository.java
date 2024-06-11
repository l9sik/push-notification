package com.poluectov.userservice.repository;

import com.poluectov.userservice.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByAuthority(String authority);

}
