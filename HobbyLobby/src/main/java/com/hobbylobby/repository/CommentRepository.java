package com.hobbylobby.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hobbylobby.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
