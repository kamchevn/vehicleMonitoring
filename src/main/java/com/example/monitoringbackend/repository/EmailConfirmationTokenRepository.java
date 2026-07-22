package com.example.monitoringbackend.repository;

import com.example.monitoringbackend.model.EmailConfirmationToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailConfirmationTokenRepository
    extends JpaRepository<EmailConfirmationToken, Long> {
  Optional<EmailConfirmationToken> findByToken(String token);

  void deleteByUserUsername(String username);
}
