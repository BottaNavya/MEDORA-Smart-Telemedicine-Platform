package com.healthconnect.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void generateToken_containsUsernameAndFutureExpiration() {
        String token = JwtUtil.generateToken("navya");
        Claims claims = Jwts.parserBuilder().setSigningKey(
                        io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                                "secretkey123secretkey123secretkey123".getBytes()))
                .build().parseClaimsJws(token).getBody();

        assertEquals("navya", claims.getSubject());
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }
}
