package com.example.recipe.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import com.example.recipe.model.User;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = "9gFbvGI2hTtiFoe6eCBWGsi/3Agotim9tH0rYKRFOF0="; // https://generate-random.org/encryption-keys

    private final Key key;

    public JwtService() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("name", user.getName())
                .claim("picture", user.getPicture())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hrs
                .signWith(key)
                .compact();
    }
}
