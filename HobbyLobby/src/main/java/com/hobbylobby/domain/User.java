package com.hobbylobby.domain;
 
import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "users")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class User {

	private Long id;
	private String name;
	private String username;
	private String password; 
	private Set<User> connections;
	private Set<Hobby> myHobbies=new HashSet<>();
	private Set<Authority> authorities = new HashSet<>();
	private String bio;
	private Set<Post> votedOn = new HashSet<>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "relation",
	            joinColumns = @JoinColumn(name = "user_id"),
	            inverseJoinColumns = @JoinColumn(name = "following_id"))
	@JsonIgnore
	public Set<User> getConnections() {
		return connections;
	}
	public void setConnections(Set<User> connections) {
		this.connections = connections;
	}
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_hobby",
			   joinColumns = {@JoinColumn(name = "user_id")},
			   inverseJoinColumns = {@JoinColumn(name = "hobby_id")})
	@JsonIgnore
	public Set<Hobby> getMyHobbies() {
		return myHobbies;
	}
	public void setMyHobbies(Set<Hobby> myHobbies) {
		this.myHobbies = myHobbies;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
	public Set<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_post",
			   joinColumns = {@JoinColumn(name = "user_id")},
			   inverseJoinColumns = {@JoinColumn(name = "post_id")})
	@JsonIgnore
	public Set<Post> getVotedOn() {
		return votedOn;
	}
	public void setVotedOn(Set<Post> votedOn) {
		this.votedOn = votedOn;
	}
	
}
