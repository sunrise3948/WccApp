package com.chasingdns.controller;

import com.chasingdns.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @GetMapping()
    public String showHome(Model model){
        return "index";
    }

    @GetMapping("/login")
    public String showLogin(Model model){
        return "login";
    }

    @GetMapping("/index")
    public String showHomePage(Model model){
        return "index";
    }

    @GetMapping("/error")
    public String showError(){
        return "error";
    }

    @GetMapping("/no_access")
    public String showAccessDenied(){
        return "no_access";
    }

}