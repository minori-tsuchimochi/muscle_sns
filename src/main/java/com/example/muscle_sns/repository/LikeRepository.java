package com.example.muscle_sns.repository;

import com.example.muscle_sns.entity.Like;
import com.example.muscle_sns.entity.Post;
import com.example.muscle_sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
  
  boolean existsByUserAndPost(User user, Post post);

  int countByPost(Post post);

  Like findByUserAndPost(User user, Post post);
}
