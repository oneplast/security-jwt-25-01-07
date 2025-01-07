package com.ll.springjwt20250107.domain.member.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthTokenServiceTest {
    @Autowired
    private AuthTokenService authTokenService;

    @Test
    @DisplayName("authTokenService 서비스가 존재한다.")
    void t1() {
        assertThat(authTokenService).isNotNull();
    }

    @Test
    @DisplayName("jjwt로 JWT 생성, {name=\"Paul\", age=23}")
    void t2() {
        // 토큰 만료기간 : 1년
        int expireSeconds = 60 * 60 * 24 * 365;
        // 토큰 시크릿 키
        Key secretKey = Keys.hmacShaKeyFor(
                "askdfhsdalkghsdaklghsdaklghsadilghewioo12i4o21498ht98ag0m1t099omaopfm-012k4epo".getBytes());

        Claims claims = Jwts.claims()
                .add("name", "Paul")
                .add("age", 23)
                .build();

        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + 1000L * expireSeconds);

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        assertThat(jwt).isNotBlank();

        System.out.println("jwt = " + jwt);
    }
}