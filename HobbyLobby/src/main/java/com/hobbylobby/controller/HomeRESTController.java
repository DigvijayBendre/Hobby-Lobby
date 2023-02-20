package com.hobbylobby.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hobbylobby.domain.User;
import com.hobbylobby.service.UserService;

@RestController
@RequestMapping("/api")
public class HomeRESTController {

    @Autowired
    private UserService userService;

    @GetMapping("/isunique/{username}")
    public boolean isUniqueUsername (@PathVariable String username) {

        User userOpt = userService.findUserByUsername(username);


        if(userOpt != null) {
            return false;
        } else {
            return true;
        }
    }

    @GetMapping("/getname/{userId}")
    public String islikedPost (@AuthenticationPrincipal User user, @PathVariable Long userId) {

        Optional<User> userOpt = userService.findUserById(userId);

        if(userOpt.isPresent()) {
            return userOpt.get().getUsername();
        } else {
            return "Name";
        }
    }
}
