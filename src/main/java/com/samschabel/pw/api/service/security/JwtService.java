package com.samschabel.pw.api.service.security;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${pw-api.jwt.secret}")
    private String secret;

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>(); 
        return createToken(claims, username); 
    }

    private String createToken(Map<String, Object> claims, String username) { 
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.MINUTE, 30);
        Date nowPlus30Min = calendar.getTime();
        SecretKey key = getSignKey();

        return Jwts.builder()
        .subject(username)
        .audience()
            .add("pw-api")
        .and()
        .claims()
            .add("role", "admin")
        .and()
        .issuer("samschabel.com")
        .issuedAt(now) // issued now
        .expiration(nowPlus30Min) // expires in 30 minutes
        .notBefore(now)
        .id(UUID.randomUUID().toString())
        .signWith(key)
        .compact();
    }  

    private SecretKey getSignKey() { 
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    } 
  
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
                .verifyWith(getSignKey()) 
                .build() 
                .parseSignedClaims(token) 
                .getPayload(); 
    } 
  
    private Boolean isTokenExpired(String token) { 
        return extractExpiration(token).before(new Date()); 
    } 
  
    public Boolean validateToken(String token, UserDetails userDetails) { 
        final String username = extractUsername(token); 
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); 
    }

}
