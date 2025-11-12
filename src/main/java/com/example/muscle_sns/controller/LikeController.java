package com.example.muscle_sns.controller;

import com.example.muscle_sns.entity.Like;
import com.example.muscle_sns.entity.Post;
import com.example.muscle_sns.entity.User;
import com.example.muscle_sns.repository.LikeRepository;
import com.example.muscle_sns.repository.PostRepository;
import com.example.muscle_sns.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/likes")
public class LikeController {

  private final LikeRepository likeRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  public LikeController(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
    this.likeRepository = likeRepository;
    this.userRepository = userRepository;
    this.postRepository = postRepository;
  }
  
  @PostMapping("/toggle")
  public String likePost(
    @RequestParam Long postId,
    @AuthenticationPrincipal UserDetails userDetails
  ) {
    User user = userRepository.findByUsername(userDetails.getUsername());
    Post post = postRepository.findById(postId).orElseThrow();

    Like existingLike = likeRepository.findByUserAndPost(user, post);

    if(existingLike != null) {
      likeRepository.delete(existingLike);
    } else {
      Like like = new Like();
      like.setUser(user);
      like.setPost(post);
      likeRepository.save(like);
    }

    return "redirect:/home";
  }
}
