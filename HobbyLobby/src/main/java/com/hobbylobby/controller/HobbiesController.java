package com.hobbylobby.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.hobbylobby.domain.Hobby;
import com.hobbylobby.domain.Post;
import com.hobbylobby.domain.User;
import com.hobbylobby.service.HobbyService;
import com.hobbylobby.service.PostService;
import com.hobbylobby.service.UserService;

@Controller
public class HobbiesController {

    @Autowired
    private UserService userService;

    @Autowired
    private HobbyService hobbyService;
    
    @Autowired
    private PostService postService;
    
    @GetMapping("/hobbies")
    public String hobbies(@AuthenticationPrincipal User user, ModelMap model) {   
    	
        List<Hobby> hobbies = hobbyService.findAllHobbies();
        List<Hobby> hobbyList = hobbyService.sortHobbiesByName(hobbies);
 
        if(user == null) {

            model.put("hobbies", hobbyList);
        } else if(user.getId() != null){
        	
            Optional<User> user1 = userService.findUserById(user.getId());
            Set<Hobby> hobbiesId = (user1.get()).getMyHobbies();
            List<Hobby> otherHobbies  = new ArrayList<>();

            for (Iterator<Hobby> it = hobbies.iterator(); it.hasNext(); ) {

                Hobby hobby = it.next();

                if(hobbiesId.contains(hobby)) {

                    continue;
                } else {

                    otherHobbies.add(hobby);
                }
            }

            otherHobbies = hobbyService.sortHobbiesByName(otherHobbies);

            model.put("hobbies", otherHobbies);
        }

        return "hobbies";
    }

    @PostMapping("/hobbies/{hobbyId}/register")
    public String register(@PathVariable Long hobbyId, @AuthenticationPrincipal User user) {   
      
        Optional<Hobby> hobby = hobbyService.findHobbyById(hobbyId);
        Optional<User> user1 = userService.findUserById(user.getId());
        
        userService.addHobby(user1.get(), hobby);
        
        return "redirect:/hobbies/"+hobbyId;
    }

    @PostMapping("/hobbies/{hobbyId}/unregister")
    public String unregister(@PathVariable Long hobbyId, @AuthenticationPrincipal User user) {   
        
        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);
        Optional<User> user1 = userService.findUserById(user.getId());

        if(hobbyOpt.isPresent()) {

            Hobby hobby=hobbyOpt.get();
            userService.removeHobby(user1.get(), hobby);
        }

        return "redirect:/hobbies/"+hobbyId;
    }


    @GetMapping("/hobbies/{hobbyId}")
    public String posts(@PathVariable Long hobbyId, ModelMap model, @AuthenticationPrincipal User loggedUser) {   

        boolean isRegistered = false;
        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);

        if(hobbyOpt.isPresent()) {

            Hobby hobby = hobbyOpt.get();

            Set<Post> postsId = hobby.getPosts();
            Set<User> users = hobby.getUsers();

            List<Post> posts = new ArrayList<>(postsId);

            posts = postService.sortPostsByDate(posts);

            for (Iterator<User> it = users.iterator(); it.hasNext(); ) {
                
                Long id = it.next().getId();
            
                if(loggedUser!= null && id.equals(loggedUser.getId())) {

                    isRegistered=true;
                }
            }

            users = userService.sortUsersByName(users);
            		
            Optional<User> user1 = userService.findUserById(loggedUser.getId());

            model.put("user", user1.get());
            
            if(loggedUser!=null) {

                model.put("isRegistered", isRegistered);
            }
            
            model.put("users", users);
            model.put("posts", posts);
            model.put("hobby", hobby);
        } else {

            model.put("errorMessage1", "No Such Hobby!");
            return "error";
        }

        return "hobby";
    }

    @GetMapping("/createhobby/{hobbyId}")
    public String createHobby(@PathVariable Long hobbyId, ModelMap model, HttpServletResponse response) throws IOException {

        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);

        if(hobbyOpt.isPresent()) {

            Hobby hobby = hobbyOpt.get();
            model.put("hobby", hobby);
        }
        
        return "hobby";
    }

}