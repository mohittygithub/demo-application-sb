package com.tyagi.demoapplication.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtils {

  @Value("${jwt.secret}")
  private String SECRET_KEY;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
      .parser()
      .setSigningKey(SECRET_KEY)
      .parseClaimsJws(token)
      .getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    String audience = userDetails
      .getAuthorities()
      .toString()
      .replaceAll("\\[", "")
      .replaceAll("\\]", "");
    return createToken(claims, userDetails.getUsername(), audience);
  }

  private String createToken(
    Map<String, Object> claims,
    String username,
    String audience
  ) {
    return Jwts
      .builder()
      .setClaims(claims)
      .setSubject(username)
      .setAudience(audience)
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(
        new Date(System.currentTimeMillis() + 1000 * 60 * 30 * 100)
      )
      // .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2))
      .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
      .compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (
      username.equals(userDetails.getUsername()) && !isTokenExpired(token)
    );
  }
}
