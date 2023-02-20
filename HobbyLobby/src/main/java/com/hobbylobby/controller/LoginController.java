package com.hobbylobby.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.hobbylobby.domain.User;
import com.hobbylobby.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal User user, ModelMap model) {

        if(user != null) {

            return "redirect:/dashboard";
        }

        return "login";
    }
    
    @GetMapping("/register")
    public String register (@AuthenticationPrincipal User user, ModelMap model) {

        if(user != null) {

            return "redirect:/dashboard";
        }
        model.put("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerPost (@ModelAttribute User user) {

        userService.registerUser(user);
        return "redirect:/dashboard";
    }
}
