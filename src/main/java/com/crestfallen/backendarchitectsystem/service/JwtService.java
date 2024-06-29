package com.crestfallen.backendarchitectsystem.service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Autowired
    private Dotenv dotenv;

//    private String SECRET_KEY = dotenv.get("jwt_secret");

//    public String generateSecretKey() {
//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey secretKey = keyGen.generateKey();
//            return Encoders.BASE64.encode(secretKey.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public String generateUsername(String username) {
        Map<String, Object> claims = new HashMap<>();


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 3))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(dotenv.get("jwt_secret"));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
