package com.example.monitoringbackend.service.domain.impl;

import com.example.monitoringbackend.service.domain.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

  private final JavaMailSender mailSender;

  @Value("${app.mail.from}")
  private String fromAddress;

  public EmailServiceImpl(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void sendConfirmationEmail(String toEmail, String username, String confirmationLink) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromAddress);
    message.setTo(toEmail);
    message.setSubject("Confirm your email");
    message.setText(
        String.format(
            "Hello %s,%n%nPlease confirm your email by opening this link:%n%s%n%nIf you did not create an account, you can ignore this email.",
            username, confirmationLink));

    try {
      mailSender.send(message);
    } catch (Exception ex) {
      LOGGER.error("Failed to send confirmation email to {}", toEmail, ex);
      throw ex;
    }
  }
}
