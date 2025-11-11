package com.example.muscle_sns.controller;

import com.example.muscle_sns.entity.Post;
import com.example.muscle_sns.entity.User;
import com.example.muscle_sns.repository.PostRepository;
import com.example.muscle_sns.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MypageController {
  
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public MypageController(PostRepository postRepository, UserRepository userRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  @GetMapping({"/mypage", "/mypage/{id}"})
  public String mypage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable(required = false) Long id, Model model) {
    User user;
      if(id != null) {
        user = userRepository.findById(id).orElseThrow();
      } else {
        user = userRepository.findByUsername(userDetails.getUsername());
      }

      List<Post> userPosts = postRepository.findByUser(user);

      model.addAttribute("username", user.getUsername());
      model.addAttribute("profileImage", user.getProfileImage());
      model.addAttribute("height", user.getHeight());
      model.addAttribute("weight", user.getWeight());
      model.addAttribute("age", user.getAge());
      model.addAttribute("location", user.getLocation());
      model.addAttribute("bio", user.getBio());
      model.addAttribute("hobbies", user.getHobbies());
      model.addAttribute("posts", userPosts);
      model.addAttribute("followingCount", user.getFollowingList().size());
      model.addAttribute("followerCount", user.getFollowerList().size());
      model.addAttribute("userId", user.getId());

      boolean isOwnPage = id == null || user.getUsername().equals(userDetails.getUsername());
      model.addAttribute("isOwnPage", isOwnPage);

    return "mypage";
  }

}
