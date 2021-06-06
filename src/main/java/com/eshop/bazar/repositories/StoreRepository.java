package com.eshop.bazar.repositories;

import com.eshop.bazar.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAll();
    Optional<Store> findById(Long id);
}
