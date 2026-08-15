package com.example.monitoringbackend.helpers;

import com.example.monitoringbackend.constants.JwtConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Final so that the configuration check in the constructor cannot leave a partially constructed
 * subclass behind.
 */
@Component
public final class JwtHelper {

  /** HS256 requires at least a 256-bit (32 byte) key. */
  private static final int MINIMUM_KEY_LENGTH_BYTES = 32;

  private final Key signingKey;

  public JwtHelper(@Value("${app.jwt.secret}") String secret) {
    this.signingKey = buildSigningKey(secret);
  }

  private static Key buildSigningKey(String secret) {
    if (secret == null || secret.isBlank()) {
      throw new IllegalStateException(
          "app.jwt.secret is not configured. Set the JWT_SECRET environment variable.");
    }

    byte[] keyBytes;
    try {
      keyBytes = Decoders.BASE64.decode(secret);
    } catch (DecodingException ex) {
      throw new IllegalStateException("app.jwt.secret must be a Base64 encoded value.", ex);
    }

    if (keyBytes.length < MINIMUM_KEY_LENGTH_BYTES) {
      throw new IllegalStateException(
          "app.jwt.secret must decode to at least "
              + MINIMUM_KEY_LENGTH_BYTES
              + " bytes, but was "
              + keyBytes.length
              + ".");
    }
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    Claims allClaims = extractAllClaims(token);
    return claimsResolver.apply(allClaims);
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private String buildToken(Map<String, Object> extraClaims, String subject, Long expiration) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("roles", userDetails.getAuthorities());
    return buildToken(extraClaims, userDetails.getUsername(), JwtConstants.EXPIRATION_TIME);
  }

  private boolean isExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public boolean isValid(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return !isExpired(token) && username.equals(userDetails.getUsername());
  }
}
