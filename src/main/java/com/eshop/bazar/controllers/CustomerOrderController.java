package com.eshop.bazar.controllers;

import com.eshop.bazar.domain.Cart;
import com.eshop.bazar.domain.CustomUser;
import com.eshop.bazar.domain.Order;
import com.eshop.bazar.domain.OrderItem;
import com.eshop.bazar.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("cart")
@RequestMapping("/customer/order")
public class CustomerOrderController {

    @Autowired
    OrderService orderService;


    @GetMapping("/list")
    public String showOrderList(Model model, Authentication authentication){
        if (!authentication.isAuthenticated())
            return "redirect:/login";
        model.addAttribute("orders", orderService.getAllListOfOrderOfUser((CustomUser)authentication.getPrincipal()));
        return "order_list";
    }

    @GetMapping("/place")
    public String placeAnOrder(@ModelAttribute("cart") Cart cart,
                               Authentication authentication){

        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setOwner((CustomUser) authentication.getPrincipal());
        order.setOrderItems(
                cart.getCartItemList()
                        .stream()
                        .map(cartItem -> {
                            OrderItem orderItem = new OrderItem();
                            orderItem.setProduct(cartItem.getProduct());
                            orderItem.setQuantity(cartItem.getQuantity());
                            orderItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
                            return orderItem;
                        }).collect(Collectors.toList())

        );
        order.setStatus(Order.STATUS.NEW);
        orderService.placeAnOrder(order);

        cart.emptyCart();

        return "redirect:/customer/order/list";
    }

    @GetMapping("/details/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model){
        Optional<Order> optionalOrder = orderService.findById(id);
        if (!optionalOrder.isPresent()){
            return "redirect:/customer/order/list";
        }
        model.addAttribute("order", optionalOrder.get());
        model.addAttribute("userProfile", optionalOrder.get().getOwner().getUserProfile());
        return "order_details";
    }

}
