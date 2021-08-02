package com.eshop.bazar.controllers;

import com.eshop.bazar.services.AwsS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/files")
public class AdminFileHandlerController {

    @Autowired
    AwsS3Service awsS3Service;

    @GetMapping
    public String showFilesAndUpload(Model model){

        model.addAttribute("files", awsS3Service.listAllImages());

        return "file_list_upload";
    }


    @PostMapping
    public String uploadFiles(Authentication authentication, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        if(authentication.getAuthorities().contains("ADMIN")){
            //  System.out.println("content type "+file.getContentType());
            if (file.getContentType().contains("image")){
                awsS3Service.upload(file);
                redirectAttributes.addFlashAttribute("message", "Uploaded successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "I accept only Images.");
            }
        }else{
            System.out.println("File not saved, you are not admin.");
        }
        return "redirect:/admin/files";
    }

    @GetMapping("/delete")
    public String deleteImage(Authentication authentication, @RequestParam String key, RedirectAttributes redirectAttributes){
        if(authentication.getAuthorities().contains("ADMIN")){
            awsS3Service.delete(key);
            redirectAttributes.addFlashAttribute("message", key+" is deleted successfully");
        }else{
            System.out.println("File not deleted, you are not admin.");
        }
        return "redirect:/admin/files";
    }


}
