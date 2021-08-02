package com.eshop.bazar.controllers;

import com.eshop.bazar.domain.Store;
import com.eshop.bazar.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/admin/stores")
public class AdminStoreController {
    @Autowired
    StoreService storeService;

    @GetMapping("/list")
    public String listStores(Model model){

        model.addAttribute("stores", storeService.getStoreList());
        return "store_list";
    }

    @GetMapping
    public String newStore(Model model){
        model.addAttribute("store", new Store());
        model.addAttribute("types", Store.TYPE.values());
        return "new_store";
    }

    @PostMapping
    public String createStore(Authentication authentication, Store store){
        if(authentication.getAuthorities().contains("ADMIN")){
            storeService.createOrUpdateStore(store);
        }else{
            System.out.println("Store not created, you are not admin.");
        }
        return "redirect:/admin/stores/list";
    }

    @GetMapping("/edit")
    public String getStoreById(@RequestParam Long id, Model model){
        Optional<Store> optionalStore = storeService.getStoreById(id);

        if (!optionalStore.isPresent())
            return "redirect:/admin/stores/list";

        model.addAttribute("store", optionalStore.get());
        model.addAttribute("types", Store.TYPE.values());
        return "new_store";
    }

    @GetMapping("/delete")
    public String deleteStore(Authentication authentication, @RequestParam Long id) {
        if(authentication.getAuthorities().contains("ADMIN")){
            storeService.deleteStore(id);
        }else{
            System.out.println("Store not deleted, you are not admin.");
        }
        return "redirect:/admin/stores/list";
    }
}
