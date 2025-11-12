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
import java.util.HashMap;
import java.util.Map;

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
  
  @PostMapping("/toggle-ajax")
  @ResponseBody
  public Map<String, Object> toggleLikeAjax(
    @RequestParam Long postId,
    @AuthenticationPrincipal UserDetails userDetails
  ) {
    User user = userRepository.findByUsername(userDetails.getUsername());
    Post post = postRepository.findById(postId).orElseThrow();

    Like existingLike = likeRepository.findByUserAndPost(user, post);
    boolean liked;

    if(existingLike != null) {
      likeRepository.delete(existingLike);
      liked = false;
    } else {
      Like like = new Like();
      like.setUser(user);
      like.setPost(post);
      likeRepository.save(like);
      liked = true;
    }

    Map<String, Object> response = new HashMap<>();
    response.put("liked", liked);
    response.put("likeCount", likeRepository.countByPost(post));

    return response;
  }
}
