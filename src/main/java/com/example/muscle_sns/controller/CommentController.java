package com.example.muscle_sns.controller;

import com.example.muscle_sns.entity.Comment;
import com.example.muscle_sns.entity.Post;
import com.example.muscle_sns.entity.User;
import com.example.muscle_sns.repository.CommentRepository;
import com.example.muscle_sns.repository.PostRepository;
import com.example.muscle_sns.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  public CommentController(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
    this.postRepository = postRepository;
  }

  @PostMapping("/add")
  public Map<String, Object> addCommentAjax(
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

    Map<String, Object> response = new HashMap<>();
    response.put("id", comment.getId());
    response.put("username", user.getUsername());
    response.put("content", comment.getContent());

    return response;
  }
  
  @PostMapping("/delete")
  public Map<String, Object>  deleteCommentAjax(@RequestParam Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
    Comment comment = commentRepository.findById(commentId).orElse(null);
    boolean success = false;
    if (comment != null && comment.getUser().getUsername().equals(userDetails.getUsername())) {
      commentRepository.delete(comment);
      success = true;
    }

    Map<String, Object> response = new HashMap<>();
    response.put("success", success);
    response.put("commentId", commentId);
    return response;
  }


  @PostMapping("/edit")
  public Map<String, Object> editCommentAjax(@RequestParam Long commentId, @RequestParam String content, @AuthenticationPrincipal UserDetails userDetails) {
    Comment comment = commentRepository.findById(commentId).orElse(null);
    boolean success = false;
    if(comment != null && comment.getUser().getUsername().equals(userDetails.getUsername())) {
      comment.setContent(content);
      commentRepository.save(comment);
      success = true;
    }
    Map<String, Object> response = new HashMap<>();
    response.put("success", success);
    response.put("commentId", commentId);
    response.put("content", comment != null ? comment.getContent() : "");
    return response;
  }
}
