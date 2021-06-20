package com.eshop.bazar.controllers;

import com.eshop.bazar.domain.Product;
import com.eshop.bazar.services.InventoryService;
import com.eshop.bazar.services.ProductService;
import com.eshop.bazar.services.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/admin/inventory")
public class AdminInventoryController {

    @Autowired
    StoreService storeService;

    @Autowired
    ProductService productService;

    @Autowired
    InventoryService inventoryService;

    @GetMapping("/accept_product")
    public String acceptProductView(@RequestParam Long id, Model model){
        Optional<Product> optionalProduct = productService.getProductById(id);

        if (!optionalProduct.isPresent())
            return "redirect:/admin/products";

        Product product = optionalProduct.get();

        model.addAttribute("product", product);
        model.addAttribute("productId", id);
        model.addAttribute("stores", storeService.getStoreList());
        return "accept_product";
    }

    @PostMapping("/accept_product")
    public String acceptProduct(@RequestParam Long productId, @RequestParam Long storeId, @RequestParam Long quantity){
        inventoryService.acceptProductToInventory(productId, storeId, quantity);
        return "redirect:/admin/inventory/list?storeId="+storeId;
    }

    @GetMapping("/list")
    public String listOfProducts(@RequestParam Long storeId, Model model){

        Boolean isFirstTimeAccess = new Boolean(false);
        String currentStoresList="";

        if (storeId == -1) {
            if(storeService.getStoreList().isEmpty()){
                isFirstTimeAccess = new Boolean(true);
            }
            else{
                model.addAttribute("inventoryList",
                        inventoryService.getProductList(storeService.getStoreList().stream().findFirst().get().getId()));
                currentStoresList=storeService.getStoreList().stream().findFirst().get().getName();
            }
        }
        else {
            model.addAttribute("inventoryList",
                    inventoryService.getProductList(storeId));
            currentStoresList=storeService.getStoreById(storeId).get().getName();
        }
        model.addAttribute("isFirstTimeAccess", isFirstTimeAccess);
        model.addAttribute("stores", storeService.getStoreList());
        model.addAttribute("currentStoresList", currentStoresList);
        return "inventory_list";
    }
}
