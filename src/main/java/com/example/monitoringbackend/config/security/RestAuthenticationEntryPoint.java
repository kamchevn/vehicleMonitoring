package com.example.monitoringbackend.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Answers unauthenticated requests with 401 and the same {"message": ...} shape used by {@code
 * GlobalExceptionHandler}. Without an explicit entry point Spring Security falls back to 403, which
 * the SPA cannot distinguish from a genuine authorization failure.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    if (response.isCommitted()) {
      return;
    }
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response
        .getWriter()
        .write("{\"message\":\"Authentication is required to access this resource.\"}");
  }
}
