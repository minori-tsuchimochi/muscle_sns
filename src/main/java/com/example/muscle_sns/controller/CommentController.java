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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

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
  
  @PostMapping("/comments/delete")
  public String deleteComment(@RequestParam Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
    Comment comment = commentRepository.findById(commentId).orElse(null);
    if (comment != null && comment.getUser().getUsername().equals(userDetails.getUsername())) {
      commentRepository.delete(comment);
    }

    return "redirect:/home";
  }

  @GetMapping("/comments/edit/{id}")
  public String editCommentForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
    Comment comment = commentRepository.findById(id).orElse(null);
    if(comment != null && comment.getUser().getUsername().equals(userDetails.getUsername())) {
      model.addAttribute("comment", comment);
      return "comment_edit";
    }
    return "redirect:/home";
  }

  @PostMapping("/comments/edit")
  public String editComment(@RequestParam Long commentId, @RequestParam String content, @AuthenticationPrincipal UserDetails userDetails) {
    Comment comment = commentRepository.findById(commentId).orElse(null);
    if(comment != null && comment.getUser().getUsername().equals(userDetails.getUsername())) {
      comment.setContent(content);
      commentRepository.save(comment);
    }
    return "redirect:/home";
  }
}
