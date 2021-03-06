package com.eshop.bazar.services;

import com.eshop.bazar.domain.CustomUser;
import com.eshop.bazar.domain.Order;
import com.eshop.bazar.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void placeAnOrder(Order order){
        orderRepository.save(order);
    }

    public List<Order> getAllListOfOrderOfUser(CustomUser owner){
        return orderRepository.getOrderByOwner(owner)
                .stream()
                .filter(order -> order.getOrderItems().size() > 0)
                .collect(Collectors.toList());
    }

    public List<Order> getListOfOrderOfUserByStatus(CustomUser owner,
                                                    Order.STATUS status){
        return orderRepository.getOrderByOwnerAndStatus(owner, status)
                .stream()
                .filter(order -> order.getOrderItems().size() > 0)
                .collect(Collectors.toList());
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllListOfOrderByStatus(Order.STATUS status) {
        return orderRepository.getOrderByStatus(status);
    }
}
