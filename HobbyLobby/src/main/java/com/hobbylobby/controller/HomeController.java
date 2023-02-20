package com.hobbylobby.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.hobbylobby.domain.Authority;
import com.hobbylobby.domain.Hobby;
import com.hobbylobby.domain.User;
import com.hobbylobby.service.HobbyService;
import com.hobbylobby.service.UserService;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private HobbyService hobbyService;
    
    @GetMapping("/")
    public String rootView (@AuthenticationPrincipal User user, ModelMap model) {

        List<Hobby> hobbies = hobbyService.findAllHobbies();

        if(user == null) {

            model.put("hobbies", hobbies);
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

        String[][] facts = { {"Rod Stewart hosted the largest ever free concert", "4"},
                            {"Singing in a group boosts mood.", "4"},
                            {"Dancers are known to be disciplined, focused and high achievers who tend to be successful students and hard workers. Research also proves that dancing also reduces stress and tension for the mind and body", "3"},
                            {"The 'Dancing Plague' of 1518 was a mania that lasted a month and killed dozens of people in Strasbourg, France through exhaustion or heart attack. People just danced uncontrollably until they collapsed!One other famous case involved people dancing on a bridge. Eventually so many people danced that they broke the bridge and fell into the river.", "3"},
                            {"A world record for the longest conga dance line was set by 119,986 people in Miami in 1988.", "3"},
                            {"Sketching can even improve your holistic health.", "5"},
                            {"Van Gogh has only sold one painting during his lifetime.", "5"},
                            {"Picasso believed that art is done to wash away the dust of our daily lives from our souls.", "5"},
                            {"The city of Monaco is smaller in size than Central Park in New York City", "9"},
                            {"Only 4% of all the world’s languages are spoken by 96% of its population", "9"},
                            {"San Marino has more cars in its country than people", "9"},
                            {"Hiking has been known to increase the satisfaction level of many types of freeze dried food, as well as several flavors of energy bars, up to 35 percent.", "10"},
                            {"Bears basically spend their entire lives hiking.", "10"},
                            {"Walking along all of Switzerland’s hiking trails would be the equivalent of going one-and-a-half times around the world.", "10"},
                            {"Reading can help prevent Alzheimer’s.", "6"},
                            {"In the Harvard Library, there are three books suspected to be bound in human skin.", "6"},
                            {"The word ‘mogigraphia’ means ‘writer’s cramp’.", "7"},
                            {"The word ‘colygraphia’ means ‘writer’s block’.", "7"},
                            {"The first programmer in the world was a woman. Her name was Ada Lovelace and she worked on an analytical engine back in the 1,800’s.", "2"},
                            {"Nowadays, there are over 700 different programming languages.", "2"},
                            {"Chopsticks were initially created for cooking, not as an eating utensil.", "1"},
                            {"Black pepper was so valuable, it used to be a currency in the Middle Ages.", "1"},
                            {"Studies indicate that surgeons who regularly play video games make 37% fewer mistakes and operate 27% faster than their peers.", "8"},
                            {"Video gamers are found to be better in multitasking, driving and navigating around the streets.", "8"},
                            {"Games provide a 23 percent gain over traditional learning.", "8"},
                            {"Video gamers are found to be better in multitasking, driving and navigating around the streets.", "8"},
                            {"The first computer virus was created in 1983.", "8"},
                            {"Elizabethan scribe Peter Bales reportedly produced a complete, handwritten copy of the Bible so small it could fit inside a walnut shell.", "7"},
                            {"The largest book ever published – “The Little Prince” – is almost 7 feet high and 10 feet wide.", "6"},
                            {"Russia is bigger than Pluto.", "9"},
                            {"Picasso believed that art is done to wash away the dust of our daily lives from our souls.", "5"} }; 


        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int day = localDate.getDayOfMonth();

        Long factHobbyId = Long.parseLong(facts[day-1][1]);
        Optional<Hobby> factHobby = hobbyService.findHobbyById(factHobbyId);
       
        model.put("fact", facts[day-1][0]);
        model.put("facthobby", factHobby.get());

        return "index";
    }

    @GetMapping("/about")
    public String about () {

        return "about";
    }

    @GetMapping("/dashboard")
    public String home(@AuthenticationPrincipal User user, ModelMap model) {

        Optional<User> user1 = userService.findUserById(user.getId());
        Set<Hobby> hobbiesId = (user1.get()).getMyHobbies();
        Set<User> usersName = (user1.get()).getConnections();
        
        List<Hobby> hobbies = new ArrayList<>(hobbiesId);

        hobbies = hobbyService.sortHobbiesByName(hobbies);
        usersName = userService.sortUsersByName(usersName);

        model.put("user", (user1.get()));
        model.put("hobbies", hobbies);
        model.put("connections", usersName);
        
        return "dashboard";
    }

    @PostMapping("/dashboard/edit")
    public String edit(User user) {

        Authority authority = new Authority();
		authority.setAuthority("ROLE_USER");
		user.getAuthorities().add(authority);

        userService.save(user);

        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard/{userId}")
    public String home(@PathVariable Long userId, ModelMap model, @AuthenticationPrincipal User loggedUser) {

        boolean isFollowing = false;
        Optional<User> userOpt = userService.findUserById(userId);

        if(userOpt.isPresent()) {

            User user = userOpt.get();

            Set<Hobby> hobbiesId = user.getMyHobbies();
            Set<User> usersName = user.getConnections();
            
            List<Hobby> hobbies = new ArrayList<>(hobbiesId);

            hobbies = hobbyService.sortHobbiesByName(hobbies);
            usersName = userService.sortUsersByName(usersName);

            model.put("user", user);
            model.put("hobbies", hobbies);
            model.put("connections", usersName);

            if(loggedUser != null) {

                Optional<User> loggedUserOpt = userService.findUserById(loggedUser.getId()); 
                User curUser = loggedUserOpt.get();
                Set<User> following = curUser.getConnections();

                for (Iterator<User> it = following.iterator(); it.hasNext(); ) {

                    User id = it.next();

                    if(loggedUser!=null && id.equals(user)) {

                        isFollowing=true;
                    }
                }

                model.put("isFollowing", isFollowing);
            }
        } else {

            model.put("errorMessage2", "Invalid User Id!");
            return "error";
        }

        return "dashboard";
    }
    
    @PostMapping("/follow/{userId}")
    public String addConnection(@AuthenticationPrincipal User user, @PathVariable Long userId) {

        Optional<User> userOpt = userService.findUserById(userId);

        if(userOpt.isPresent()) {

            Optional<User> user1 = userService.findUserById(user.getId());
            userService.addConnection(user1.get(), userOpt.get());
        }

        return "redirect:/dashboard/" + userId;
    }

    @PostMapping("/unfollow/{userId}")
    public String removeConnection(@AuthenticationPrincipal User user, @PathVariable Long userId) {

        Optional<User> userOpt = userService.findUserById(userId);

        if(userOpt.isPresent()) {

            Optional<User> user1 = userService.findUserById(user.getId());
            userService.removeConnection(user1.get(), userOpt.get());
        }
        
        return "redirect:/dashboard/" + userId;
    }
}
