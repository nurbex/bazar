package com.eshop.bazar.controllers;

import com.eshop.bazar.domain.Product;
import com.eshop.bazar.services.ImageService;
import com.eshop.bazar.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    @Autowired
    ProductService productService;

    @Autowired
    ImageService imageService;

    @GetMapping
    public String getProducts(Model model){
        model.addAttribute("products", productService.getAllProducts());
        return "product_list";
    }
    
    @GetMapping("page/{pNum}")
    public String getProductsPaged(@PathVariable int pNum, Model model){
        int pageSize = 3;

        Page<Product> productsPage = productService.getPaginatedList(pNum, pageSize);
        List<Product> productList = productsPage.getContent();

        model.addAttribute("currentPage", pNum);
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("totalItem", productsPage.getTotalElements());
        model.addAttribute("products", productList);
        return "product_list";
    }

    @GetMapping("/new")
    public String newProduct( Model model){

        model.addAttribute("images", imageService.getImageList());
        model.addAttribute("product", new Product());
        return "new_product";
    }

    @PostMapping
    public String createProduct(Authentication authentication, Product product){
        if(authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN"))){
            productService.createOrUpdateProduct(product);
        }else{
            System.out.println("Product not created, you are not admin.");
        }
        return "redirect:/admin/products/page/0";
    }

    @GetMapping("/delete")
    public String deleteProduct(Authentication authentication, @RequestParam Long id){
        if(authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN"))){
            productService.deleteProduct(id);
        }else{
            System.out.println("Product not deleted, you are not admin.");
        }
        return "redirect:/admin/products/page/0";
    }

    @GetMapping("/edit")
    public String editProduct(@RequestParam Long id, Model model){

        Optional<Product> productOptional = productService.getProductById(id);
        if (!productOptional.isPresent())
            return "redirect:/admin/products";
        model.addAttribute("imageList", imageService.getImageList());
        model.addAttribute("product", productOptional.get());
        return "edit_product";
    }

    @PostMapping("/edit")
    public String saveProduct(Authentication authentication, Product product){
        if(authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN"))){
            productService.createOrUpdateProduct(product);
        }else{
            System.out.println("Product not created, you are not admin.");
        }
        return "redirect:/admin/products";
    }
}
