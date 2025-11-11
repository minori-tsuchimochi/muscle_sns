package com.example.muscle_sns.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "followers")
public class Follower {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "follower_id", nullable = false)
  private User follower;

  @ManyToOne
  @JoinColumn(name = "following_id", nullable = false)
  private User following;
}
