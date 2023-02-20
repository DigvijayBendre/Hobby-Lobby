package com.hobbylobby.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hobbylobby.domain.Authority;
import com.hobbylobby.domain.Hobby;
import com.hobbylobby.domain.User;
import com.hobbylobby.repository.UserRepository;

@Service
public class UserService {

	@Autowired
    private UserRepository userRepository;

    @Autowired
	private PasswordEncoder passwordEncoder;

    public Optional<User> findUserById(Long id) {
    	
    	if(id != null) {
    		
    		return userRepository.findById(id);
    	} else {
    		return null;
    	}
    	 	
    }

    public User findUserByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    public Set<User> sortUsersByName(Set<User> users) {
    	
    	Set<User> sorted = new TreeSet<User>(new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user1.getName().compareTo(user2.getName());
            }
        });

        sorted.addAll(users);
        
        return sorted;
    }

    public List<String> sortUsersByName2(List<String> users) {

        Collections.sort(users, new Comparator<String>(){
            @Override
            public int compare(String user1, String user2) {
                return user1.compareTo(user2);
            }
        });

        return users;
    }

    public User registerUser(User user) throws AlreadyExistsException {

		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		Authority authority = new Authority();
		authority.setAuthority("ROLE_USER");
		authority.setUser(user);
		user.getAuthorities().add(authority);

		User users = userRepository.findByUsername(user.getUsername());

        if(users == null) {

		    return userRepository.save(user);
        } else {

            throw new AlreadyExistsException("Username already exists!");
        }
	}

    public void addHobby(User user, Optional<Hobby> hobbyOpt) {

        if(hobbyOpt.isPresent()) {

            Hobby hobby=hobbyOpt.get();

            user.getMyHobbies().add(hobby);
            userRepository.save(user);
            
        }

        return;
    }

    public void removeHobby(User user, Hobby hobby) {

        user.getMyHobbies().remove(hobby);

        userRepository.save(user);
        
        return;
    }

    public void addConnection(User user1, User user2) {

        user1.getConnections().add(user2);
        userRepository.save(user1);

        return;
    }

    public void removeConnection(User user1, User user2) {

        user1.getConnections().remove(user2);
        userRepository.save(user1);

        return;
    }

    public User save(User user) {

        return userRepository.save(user);
    }

    public void deleteUser(User user) {

        userRepository.deleteById(user.getId());
        return;
    }
	
}
