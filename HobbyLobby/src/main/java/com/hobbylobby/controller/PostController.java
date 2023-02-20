package com.hobbylobby.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hobbylobby.domain.Comment;
import com.hobbylobby.domain.Hobby;
import com.hobbylobby.domain.Post;
import com.hobbylobby.domain.User;
import com.hobbylobby.service.CommentService;
import com.hobbylobby.service.HobbyService;
import com.hobbylobby.service.PostService;
import com.hobbylobby.service.UserService;

@Controller
@RequestMapping("/hobbies/{hobbyId}/post")
public class PostController {

    @Autowired
    private UserService userService;

    @Autowired
    private HobbyService hobbyService;
    
    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;
    
    @GetMapping("")
    public String createPost (ModelMap model, @PathVariable Long hobbyId) {

        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);

        if(hobbyOpt.isPresent()) {

            model.put("post", new Post());
        } else {

            model.put("errorMessage3","Invalid Hobby Id!");
            return "error";
        }

        return "post";
    }
    
    @PostMapping("")
    public String uploadPost (@PathVariable Long hobbyId, @AuthenticationPrincipal User user, @ModelAttribute Post post) {
        
        post.setUserId(user.getId());
        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);

        if(hobbyOpt.isPresent()) {

            Hobby hobby = hobbyOpt.get();
            post = postService.uploadPost(hobby, post);
        }
        
        return "redirect:/hobbies/"+hobbyId;
    }

    @GetMapping("/{postId}")
    public String displayPost(@PathVariable Long hobbyId, @PathVariable Long postId, ModelMap model) {

        Optional<Post> postOpt = postService.findPostById(postId);
        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);

        if(!hobbyOpt.isPresent()) {

            model.put("errorMessage4", "Invalid Hobby Id!");
            return "error";
        }

        if(postOpt.isPresent()) {

            Post post = postOpt.get();
            Hobby hobby = hobbyOpt.get();

            model.put("post", post);
            model.put("hobby", hobby);
            model.put("rootComment", new Comment());

            Set<User> usersVoted = post.getUsersVoted();
            Long authorId = post.getUserId();
            Optional<User> authorOpt = userService.findUserById(authorId);

            if(authorOpt.isPresent()) {

                model.put("author", authorOpt.get());
            }
            
            Set<Comment> comments = post.getComments();
            List<Comment> comment  = new ArrayList<>(comments);
            
            comment = commentService.sortCommentsByDate(comment);
            usersVoted = userService.sortUsersByName(usersVoted);

            model.put("comments", comment);
            model.put("usersVoted", usersVoted);
        } else {

            model.put("errorMessage5", "Post does not exists!");
            return "error";
        }

        return "postdisplay";
    }

    @PostMapping("/like/{postId}")
    public String likePost (@AuthenticationPrincipal User user, @PathVariable Long postId, @PathVariable Long hobbyId) {

        Optional<Post> postOpt = postService.findPostById(postId);

        if(postOpt.isPresent()) {

            Optional<User> user1 = userService.findUserById(user.getId());
            postService.likePost(postOpt.get(), user1.get());
        }

        return "redirect:/hobbies/"+hobbyId;
    }
}