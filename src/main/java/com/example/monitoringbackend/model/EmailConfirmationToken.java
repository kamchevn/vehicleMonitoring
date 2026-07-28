package com.example.monitoringbackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class EmailConfirmationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String token;

  @OneToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "username", nullable = false)
  private User user;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  private boolean used = false;

  public EmailConfirmationToken() {}

  public EmailConfirmationToken(String token, User user, LocalDateTime expiresAt) {
    this.token = token;
    this.user = user;
    this.expiresAt = expiresAt;
  }

  public Long getId() {
    return id;
  }

  public String getToken() {
    return token;
  }

  public User getUser() {
    return user;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public boolean isUsed() {
    return used;
  }

  public void setUsed(boolean used) {
    this.used = used;
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiresAt);
  }
}
