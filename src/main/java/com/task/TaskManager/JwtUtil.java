package com.task.TaskManager;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "";

    public String generateToken(String username) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 86400000))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
    }

    public String extractUserName(String token) {
        return Jwts.parserBuilder()
        .setSigningKey(SECRET_KEY.getBytes()).build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    }

    public boolean validateToken(String token, String username) {
        return username.equals(extractUserName(token)) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parserBuilder()
        .setSigningKey(SECRET_KEY.getBytes()).build()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration();

        return expirationDate.before(new Date());
    }

}
