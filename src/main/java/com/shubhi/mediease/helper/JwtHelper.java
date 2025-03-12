package com.shubhi.mediease.helper;

import com.shubhi.mediease.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {

    private final String SECRET_KEY = "C2154BF222A336473C81B11EA2DB5C2154BF222A336473C81B11EA2DB5C2154BF222A336473C81B11EA2DB5";

    // Extract Email (Subject) from Token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract Expiration Date from Token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract Role from Token
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Extract Permissions from Token
    public List<String> extractPermissions(String token) {
        return extractClaim(token, claims -> claims.get("permissions", List.class));
    }

    // Extract a Specific Claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract All Claims from Token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
    public Map<String, Object> extractRoleAndPermissions(String token) {
        Claims claims = extractAllClaims(token);
        Map<String, Object> rolePermissions = new HashMap<>();
        rolePermissions.put("role", claims.get("role", String.class));
        rolePermissions.put("permissions", claims.get("permissions", List.class));
        return rolePermissions;
    }

    // Check if Token is Expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate Token with Email, Role, and Permissions
    public String generateToken(String email, Role role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());  // Store role as string
        claims.put("permissions", role.getPermissions());  // Store permissions as a list

        return createToken(claims, email);
    }

    // Create Token with Claims
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour validity
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Validate Token
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
