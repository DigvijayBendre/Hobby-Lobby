package com.hobbylobby.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hobbylobby.domain.Hobby;
import com.hobbylobby.domain.Post;
import com.hobbylobby.domain.User;
import com.hobbylobby.repository.PostRepository;

@Service
public class PostService {
	
	 @Autowired
	    private PostRepository postRepository;

	    @Autowired 
	    private HobbyService hobbyService;

	    @Autowired
	    private UserService userService;

	    public Optional<Post> findPostById(Long id) {

	        return postRepository.findById(id);
	    }
	    
	    public List<Post> sortPostsByDate(List<Post> posts) {

	        Collections.sort(posts, new Comparator<Post>(){
	            @Override
	            public int compare(Post post1, Post post2) {
	                return post2.getCreatedDate().compareTo(post1.getCreatedDate());
	            }
	        });

	        return posts;
	    }

	    public Post uploadPost(Hobby hobby, Post post) {

	        Set<Post> posts = hobby.getPosts();
	        
	        post.setHobby(hobby);
	        post.setCreatedDate(new Date());
	        postRepository.save(post);
	        posts.add(post);

	        hobbyService.save(hobby);

	        return post;
	    }

	    public void likePost(Post post, User user) {

	        if(post.getUsersVoted().contains(user)){

	            post.getUsersVoted().remove(user);
	            user.getVotedOn().remove(post);

	            long votes=post.getVotes();
	            votes--;

	            post.setVotes(votes);
	        } else {

	            post.getUsersVoted().add(user);
	            user.getVotedOn().add(post);
	            
	            long votes=post.getVotes();
	            votes++;

	            post.setVotes(votes);
	        }

	        postRepository.save(post);
	        userService.save(user);

	        return;
	    }

	    public Post save(Post post) {

	        return postRepository.save(post);
	    }

}
