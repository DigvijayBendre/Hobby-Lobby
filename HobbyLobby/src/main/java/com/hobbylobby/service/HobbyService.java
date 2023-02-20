package com.hobbylobby.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hobbylobby.domain.Hobby;
import com.hobbylobby.domain.User;
import com.hobbylobby.repository.HobbyRepository;

@Service
public class HobbyService {
	
	@Autowired
    private HobbyRepository hobbyRepository;

    public Optional<Hobby> findHobbyById(Long id) {

        return hobbyRepository.findById(id);
    }

    public List<Hobby> findAllHobbies() {

        return hobbyRepository.findAll();
    }

    public List<Hobby> sortHobbiesByName(List<Hobby> hobbies) {

        Collections.sort(hobbies, new Comparator<Hobby>(){
            @Override
            public int compare(Hobby hobby1, Hobby hobby2) {
                return hobby1.getName().compareTo(hobby2.getName());
            }
        });

        return hobbies;
    }
    
    public Hobby save(Hobby hobby) {

        return hobbyRepository.save(hobby);
    }

}
