package com.ecom.admin.controller;


import com.ecom.library.dto.AdminDto;
import com.ecom.library.model.Admin;
import com.ecom.library.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AdminService adminService;

    private final BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("title", "Login Page");
        return "login";
    }

    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("title", "Home Page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        return "index";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model){
        model.addAttribute("title", "Forgot Password");
        return "forgot-password";
    }

    public String addNewAdmin(@Valid @ModelAttribute("adminDto") AdminDto adminDto,
                              BindingResult result,
                              Model model){
        try{
            if(result.hasErrors()){
                model.addAttribute("adminDto", adminDto);
                result.toString();
                return "register";
            }

            String username = adminDto.getUsername();
            Admin admin = adminService.findByUsername(username);
            if(admin !=null){
                model.addAttribute("adminDto", adminDto);
                System.out.println("admin not null");
                model.addAttribute("emailError", "Your email has been registered!");
                return "register";
            }

            if(adminDto.getPassword().equals(adminDto.getRepeatPassword())){
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                adminService.save(adminDto);
                System.out.println("success");
                model.addAttribute("success", "Register successfully");
                model.addAttribute("adminDto", adminDto);
            }else{
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("passwordError", "Your password maybe wrong ! Check Again");
                System.out.println("password not same");
                return "register";
            }
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("error", "The server has been wrong");
        }

        return "register";
    }

}
