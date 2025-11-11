package com.example.muscle_sns.repository;

import com.example.muscle_sns.entity.Follower;
import com.example.muscle_sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

  boolean existsByFollowerAndFollowing(User follower, User following);

  void deleteByFollowerAndFollowing(User follower, User following);
  
  int countByFollowing(User user);

  int countByFollower(User user);
}
