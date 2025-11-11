package com.example.muscle_sns.controller;

import com.example.muscle_sns.entity.Follower;
import com.example.muscle_sns.entity.User;
import com.example.muscle_sns.repository.FollowerRepository;
import com.example.muscle_sns.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/follow")
public class FollowController {

  private final FollowerRepository followerRepository;
  private final UserRepository userRepository;

  public FollowController(FollowerRepository followerRepository, UserRepository userRepository) {
    this.followerRepository = followerRepository;
    this.userRepository = userRepository;
  }
  
  @Transactional
  @PostMapping("/toggle")
  public String toggleFollow(
    @RequestParam Long userId,
    @AuthenticationPrincipal UserDetails userDetails
  ) {
    User follower = userRepository.findByUsername(userDetails.getUsername());
    User following = userRepository.findById(userId).orElseThrow();

    if(follower.getId().equals(following.getId())) {
      return "redirect:/home";
    }

    if(followerRepository.existsByFollowerAndFollowing(follower, following)) {
      followerRepository.deleteByFollowerAndFollowing(follower, following);
    } else {
      Follower f = new Follower();
      f.setFollower(follower);
      f.setFollowing(following);
      followerRepository.save(f);
    }

    return "redirect:/home";
  }
}
