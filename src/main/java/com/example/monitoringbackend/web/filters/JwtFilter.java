package com.example.monitoringbackend.web.filters;

import com.example.monitoringbackend.config.security.PublicEndpoints;
import com.example.monitoringbackend.config.security.RestAuthenticationEntryPoint;
import com.example.monitoringbackend.constants.JwtConstants;
import com.example.monitoringbackend.exceptions.UserNotFoundException;
import com.example.monitoringbackend.helpers.JwtHelper;
import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.service.domain.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

  private final JwtHelper jwtHelper;
  private final UserService userService;
  private final RestAuthenticationEntryPoint authenticationEntryPoint;

  public JwtFilter(
      JwtHelper jwtHelper,
      UserService userService,
      RestAuthenticationEntryPoint authenticationEntryPoint) {
    this.jwtHelper = jwtHelper;
    this.userService = userService;
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  /**
   * The SPA attaches its bearer token to every call, including the login endpoint. Ignoring tokens
   * on public paths means a stale token cannot lock a visitor out of logging in again.
   */
  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    String path = request.getRequestURI().substring(request.getContextPath().length());
    for (String pattern : PublicEndpoints.patterns()) {
      if (PATH_MATCHER.match(pattern, path)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      filterChain.doFilter(request, response);
      return;
    }

    String headerValue = request.getHeader(JwtConstants.HEADER);
    if (headerValue == null || !headerValue.startsWith(JwtConstants.TOKEN_PREFIX)) {
      // No credentials presented. Authorization rules decide whether that is acceptable.
      filterChain.doFilter(request, response);
      return;
    }

    String token = headerValue.substring(JwtConstants.TOKEN_PREFIX.length());

    try {
      authenticate(request, token);
    } catch (JwtException | AuthenticationException | UserNotFoundException ex) {
      // A presented-but-unusable token is an authentication failure, not an anonymous request.
      SecurityContextHolder.clearContext();
      authenticationEntryPoint.commence(
          request, response, new BadCredentialsException("Invalid authentication token.", ex));
      return;
    }

    filterChain.doFilter(request, response);
  }

  private void authenticate(HttpServletRequest request, String token) {
    Authentication existingAuthentication = SecurityContextHolder.getContext().getAuthentication();
    if (existingAuthentication != null) {
      return;
    }

    String username = jwtHelper.extractUsername(token);
    if (username == null) {
      throw new BadCredentialsException("Token carries no subject.");
    }

    User user = userService.getUser(username);
    if (!jwtHelper.isValid(token, user)) {
      throw new BadCredentialsException("Token is expired or does not match its subject.");
    }
    if (!user.isEnabled()) {
      // Confirmation revoked or account disabled after the token was issued.
      throw new DisabledException("Account is not enabled.");
    }

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }
}
