package com.example.muscle_sns.entity;

import java.util.List;
import com.example.muscle_sns.entity.Post;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  private String profileImage;

  private Double height;  //身長
  private Double weight;  //体重
  private Integer age;  //年齢
  private String location;  //住んでるところ
  private String bio;  //自己紹介
  private String hobbies;  //趣味

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Post> posts;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Like> likes;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Comment> comments;
}