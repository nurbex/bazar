package com.eshop.bazar.repositories;

import com.eshop.bazar.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authority, Long> {
    Authority findAuthorityByAuthority(String authority);
}
