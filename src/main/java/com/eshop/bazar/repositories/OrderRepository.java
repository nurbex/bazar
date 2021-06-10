package com.eshop.bazar.repositories;

import com.eshop.bazar.domain.CustomUser;
import com.eshop.bazar.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getOrderByOwner(CustomUser owner);
    List<Order> getOrderByOwnerAndStatus(CustomUser owner, Order.STATUS status);

    List<Order> getOrderByStatus(Order.STATUS status);
}
