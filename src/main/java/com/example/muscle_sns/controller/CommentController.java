package com.example.muscle_sns.controller;

import com.example.muscle_sns.entity.Comment;
import com.example.muscle_sns.entity.Post;
import com.example.muscle_sns.entity.User;
import com.example.muscle_sns.repository.CommentRepository;
import com.example.muscle_sns.repository.PostRepository;
import com.example.muscle_sns.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  public CommentController(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
    this.postRepository = postRepository;
  }

  @PostMapping("/comments/add")
  public String addComment(
    @RequestParam Long postId,
    @RequestParam String content,
    @AuthenticationPrincipal UserDetails userDetails
  ) {
    User user = userRepository.findByUsername(userDetails.getUsername());
    Post post = postRepository.findById(postId).orElseThrow();

    Comment comment = new Comment();
    comment.setUser(user);
    comment.setPost(post);
    comment.setContent(content);

    commentRepository.save(comment);

    return "redirect:/home";
  }
  
}
