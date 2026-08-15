package com.example.monitoringbackend.web.filters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.monitoringbackend.config.security.RestAuthenticationEntryPoint;
import com.example.monitoringbackend.exceptions.UserNotFoundException;
import com.example.monitoringbackend.helpers.JwtHelper;
import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.enumerations.Role;
import com.example.monitoringbackend.service.domain.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

  @Mock private JwtHelper jwtHelper;
  @Mock private UserService userService;
  @Mock private FilterChain filterChain;

  private JwtFilter jwtFilter;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    jwtFilter = new JwtFilter(jwtHelper, userService, new RestAuthenticationEntryPoint());
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    SecurityContextHolder.clearContext();
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  private static User user(String username, boolean enabled) {
    User user = new User(username, "encoded", "a@example.com", "N", "S", Role.ROLE_USER);
    user.setEnabled(enabled);
    return user;
  }

  private Authentication currentAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  @Test
  void validToken_authenticatesAndContinuesTheChain() throws Exception {
    User alice = user("alice", true);
    request.addHeader("Authorization", "Bearer valid-token");
    when(jwtHelper.extractUsername("valid-token")).thenReturn("alice");
    when(userService.getUser("alice")).thenReturn(alice);
    when(jwtHelper.isValid("valid-token", alice)).thenReturn(true);

    jwtFilter.doFilter(request, response, filterChain);

    assertNotNull(currentAuthentication());
    assertEquals(alice, currentAuthentication().getPrincipal());
    verify(filterChain).doFilter(request, response);
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  void noAuthorizationHeader_continuesUnauthenticated() throws Exception {
    jwtFilter.doFilter(request, response, filterChain);

    assertNull(currentAuthentication());
    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(jwtHelper, userService);
  }

  @Test
  void nonBearerHeader_continuesUnauthenticated() throws Exception {
    request.addHeader("Authorization", "Basic dXNlcjpwYXNz");

    jwtFilter.doFilter(request, response, filterChain);

    assertNull(currentAuthentication());
    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(jwtHelper, userService);
  }

  @Test
  void malformedToken_isRejectedWith401AndStopsTheChain() throws Exception {
    request.addHeader("Authorization", "Bearer garbage");
    when(jwtHelper.extractUsername("garbage"))
        .thenThrow(new io.jsonwebtoken.MalformedJwtException("bad token"));

    jwtFilter.doFilter(request, response, filterChain);

    assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    assertNull(currentAuthentication());
    verify(filterChain, never()).doFilter(any(), any());
  }

  @Test
  void expiredToken_isRejectedWith401AndStopsTheChain() throws Exception {
    request.addHeader("Authorization", "Bearer expired");
    when(jwtHelper.extractUsername("expired"))
        .thenThrow(new ExpiredJwtException(null, null, "expired"));

    jwtFilter.doFilter(request, response, filterChain);

    assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    assertNull(currentAuthentication());
    verify(filterChain, never()).doFilter(any(), any());
  }

  @Test
  void tokenFailingValidation_isRejectedWith401() throws Exception {
    User alice = user("alice", true);
    request.addHeader("Authorization", "Bearer stale");
    when(jwtHelper.extractUsername("stale")).thenReturn("alice");
    when(userService.getUser("alice")).thenReturn(alice);
    when(jwtHelper.isValid("stale", alice)).thenReturn(false);

    jwtFilter.doFilter(request, response, filterChain);

    assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    assertNull(currentAuthentication());
    verify(filterChain, never()).doFilter(any(), any());
  }

  @Test
  void disabledUser_isRejectedWith401EvenWithAValidToken() throws Exception {
    User disabled = user("alice", false);
    request.addHeader("Authorization", "Bearer valid-token");
    when(jwtHelper.extractUsername("valid-token")).thenReturn("alice");
    when(userService.getUser("alice")).thenReturn(disabled);
    when(jwtHelper.isValid("valid-token", disabled)).thenReturn(true);

    jwtFilter.doFilter(request, response, filterChain);

    assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    assertNull(currentAuthentication());
    verify(filterChain, never()).doFilter(any(), any());
  }

  @Test
  void tokenForDeletedUser_isRejectedWith401() throws Exception {
    request.addHeader("Authorization", "Bearer valid-token");
    when(jwtHelper.extractUsername("valid-token")).thenReturn("ghost");
    when(userService.getUser("ghost")).thenThrow(new UserNotFoundException("ghost"));

    jwtFilter.doFilter(request, response, filterChain);

    assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    assertNull(currentAuthentication());
    verify(filterChain, never()).doFilter(any(), any());
  }

  @Test
  void publicEndpoints_ignoreTokensEntirely() throws Exception {
    // A visitor whose token expired must still be able to log in again.
    request.setRequestURI("/api/user/login");
    request.setMethod("POST");
    request.addHeader("Authorization", "Bearer expired");

    jwtFilter.doFilter(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    verifyNoInteractions(jwtHelper, userService);
  }

  @Test
  void healthEndpoint_ignoresTokensEntirely() throws Exception {
    request.setRequestURI("/actuator/health");
    request.addHeader("Authorization", "Bearer garbage");

    jwtFilter.doFilter(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(jwtHelper, userService);
  }

  @Test
  void protectedEndpoints_areStillFiltered() throws Exception {
    request.setRequestURI("/api/vehicle");
    request.addHeader("Authorization", "Bearer garbage");
    when(jwtHelper.extractUsername("garbage"))
        .thenThrow(new io.jsonwebtoken.MalformedJwtException("bad token"));

    jwtFilter.doFilter(request, response, filterChain);

    assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    verify(filterChain, never()).doFilter(any(), any());
  }

  @Test
  void preflightRequest_bypassesTokenProcessing() throws Exception {
    request.setMethod("OPTIONS");
    request.addHeader("Authorization", "Bearer whatever");

    jwtFilter.doFilter(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(jwtHelper, userService);
  }
}
