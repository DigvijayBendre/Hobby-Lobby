package com.hobbylobby.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hobbylobby.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
