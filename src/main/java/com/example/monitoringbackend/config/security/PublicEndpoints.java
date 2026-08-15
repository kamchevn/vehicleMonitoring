package com.example.monitoringbackend.config.security;

import java.util.List;

/**
 * Single source of truth for the endpoints that must work without authentication.
 *
 * <p>Used both by the authorization rules and by {@code JwtFilter}, which has to ignore tokens on
 * these paths. Otherwise a visitor holding an expired token could not log in again, because the
 * filter would reject the login request before it reached the controller.
 */
public final class PublicEndpoints {

  private static final List<String> PATTERNS =
      List.of(
          "/api/user/register",
          "/api/user/confirm",
          "/api/user/login",
          "/actuator/health",
          "/swagger-ui/**",
          "/swagger-ui.html",
          "/v3/api-docs/**");

  private PublicEndpoints() {}

  public static List<String> patterns() {
    return PATTERNS;
  }

  /** Fresh array for APIs that take varargs patterns, so the allowlist itself stays immutable. */
  public static String[] patternsArray() {
    return PATTERNS.toArray(new String[0]);
  }
}
