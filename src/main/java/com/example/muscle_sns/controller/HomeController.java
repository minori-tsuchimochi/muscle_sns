package com.example.muscle_sns.controller;

import com.example.muscle_sns.entity.Post;
import com.example.muscle_sns.entity.User;
import com.example.muscle_sns.repository.LikeRepository;
import com.example.muscle_sns.repository.PostRepository;
import com.example.muscle_sns.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final LikeRepository likeRepository;

  public HomeController(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.likeRepository = likeRepository;
  }

  @GetMapping("/home")
  public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    if(userDetails != null) {
      model.addAttribute("username", userDetails.getUsername());
    }

    User loginUser = userRepository.findByUsername(userDetails.getUsername());
    List<Post> posts = postRepository.findAll();

    Map<Long, Integer> likeCounts = new HashMap<>();
    Map<Long, Boolean> likeByUser = new HashMap<>();

    for (Post post : posts) {
      likeCounts.put(post.getId(), likeRepository.countByPost(post));
      likeByUser.put(post.getId(), likeRepository.existsByUserAndPost(loginUser, post));
    }


    model.addAttribute("posts", posts);
    model.addAttribute("likeCounts", likeCounts);
    model.addAttribute("likeByUser", likeByUser);

    return "home";
  }
  
}
