package com.example.monitoringbackend.helpers;

import static org.junit.jupiter.api.Assertions.*;

import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.enumerations.Role;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Test;

class JwtHelperTest {

  private static final String SECRET = base64Of("monitoring-backend-local-test!!!");
  private static final String OTHER_SECRET = base64Of("a-completely-different-key!!!!!!!");

  private static String base64Of(String raw) {
    return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
  }

  private static User enabledUser(String username) {
    User user = new User(username, "encoded", username + "@example.com", "N", "S", Role.ROLE_USER);
    user.setEnabled(true);
    return user;
  }

  @Test
  void generatedToken_carriesSubjectAndIsValidForThatUser() {
    JwtHelper jwtHelper = new JwtHelper(SECRET);
    User user = enabledUser("alice");

    String token = jwtHelper.generateToken(user);

    assertEquals("alice", jwtHelper.extractUsername(token));
    assertTrue(jwtHelper.isValid(token, user));
  }

  @Test
  void token_isNotValidForADifferentUser() {
    JwtHelper jwtHelper = new JwtHelper(SECRET);

    String token = jwtHelper.generateToken(enabledUser("alice"));

    assertFalse(jwtHelper.isValid(token, enabledUser("bob")));
  }

  @Test
  void tokenSignedWithAnotherKey_isRejected() {
    String foreignToken = new JwtHelper(OTHER_SECRET).generateToken(enabledUser("alice"));
    JwtHelper jwtHelper = new JwtHelper(SECRET);

    assertThrows(SignatureException.class, () -> jwtHelper.extractUsername(foreignToken));
  }

  @Test
  void tamperedToken_isRejected() {
    JwtHelper jwtHelper = new JwtHelper(SECRET);
    String token = jwtHelper.generateToken(enabledUser("alice"));
    String tampered = token.substring(0, token.length() - 2) + "xy";

    assertThrows(RuntimeException.class, () -> jwtHelper.extractUsername(tampered));
  }

  @Test
  void missingSecret_failsFast() {
    IllegalStateException blank =
        assertThrows(IllegalStateException.class, () -> new JwtHelper(""));
    assertTrue(blank.getMessage().contains("JWT_SECRET"));
    assertThrows(IllegalStateException.class, () -> new JwtHelper(null));
  }

  @Test
  void secretShorterThan256Bits_failsFast() {
    String tooShort = base64Of("only-15-bytes!!");

    IllegalStateException ex =
        assertThrows(IllegalStateException.class, () -> new JwtHelper(tooShort));

    assertTrue(ex.getMessage().contains("32 bytes"));
  }
}
