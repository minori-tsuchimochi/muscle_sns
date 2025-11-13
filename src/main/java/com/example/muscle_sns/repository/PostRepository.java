package com.example.muscle_sns.repository;

import com.example.muscle_sns.entity.Post;
import com.example.muscle_sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
  List<Post> findByUserOrderByCreatedAtDesc(User user);

  List<Post> findAllByOrderByCreatedAtDesc();
}
