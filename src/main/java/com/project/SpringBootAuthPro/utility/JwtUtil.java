package com.project.SpringBootAuthPro.utility;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	private static final String SECRET_KEY = "Vf9^s8Lr#3xP1!kTz7$eRq@0YmB*6GpJwUoX2hNs+dFgMt!vShQ5LbDzA9E$CtRn";
	
	private final SecretKey key  = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	
	public String generateToken(String username, long expirationMillis) {
	    return Jwts.builder()
	            .setSubject(username)
	            .setIssuedAt(new Date())
	            .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
	            .signWith(key, SignatureAlgorithm.HS256)
	            .compact();
	}

	
   public String extractUsername(String token) {
       return extractClaim(token, Claims::getSubject);
   }

   public boolean validateToken(String token, UserDetails userDetails) {
       final String username = extractUsername(token);
       return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
   }

   private boolean isTokenExpired(String token) {
       return extractClaim(token, Claims::getExpiration).before(new Date());
   }
   private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
       Claims claims = Jwts
           .parserBuilder()
           .setSigningKey(key)
           .build()
           .parseClaimsJws(token)
           .getBody();   
       return claimsResolver.apply(claims);
   }
}
