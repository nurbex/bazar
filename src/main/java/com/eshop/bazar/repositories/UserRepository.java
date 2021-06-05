package com.eshop.bazar.repositories;

import com.eshop.bazar.domain.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Long> {
    CustomUser findCustomUserByUsername(String username);
}
