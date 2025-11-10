package com.example.muscle_sns.repository;

import com.example.muscle_sns.entity.Comment;
import com.example.muscle_sns.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByPost(Post post);
}
