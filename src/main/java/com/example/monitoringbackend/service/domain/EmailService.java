package com.example.monitoringbackend.service.domain;

public interface EmailService {
  void sendConfirmationEmail(String toEmail, String username, String confirmationLink);
}
