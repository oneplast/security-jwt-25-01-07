package com.ll.springjwt20250107.domain.member.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.ll.springjwt20250107.domain.member.member.entity.Member;
import com.ll.springjwt20250107.util.Ut;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
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

    @Autowired
    private MemberService memberService;

    // 토큰 만료기간 : 1년
    private int expireSeconds = 60 * 60 * 24 * 365;

    // 토큰 시크릿 키
    private String secret = "askdfhsdalkghsdaklghsdaklghsadilghewioo12i4o21498ht98ag0m1t099omaopfm-012k4epo";

    @Test
    @DisplayName("authTokenService 서비스가 존재한다.")
    void t1() {
        assertThat(authTokenService).isNotNull();
    }

    @Test
    @DisplayName("jjwt로 JWT 생성, {name=\"Paul\", age=23}")
    void t2() {
        Claims claims = Jwts.claims()
                .add("name", "Paul")
                .add("age", 23)
                .build();

        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + 1000L * expireSeconds);

        Key secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        assertThat(jwt).isNotBlank();

        System.out.println("jwt = " + jwt);
    }

    @Test
    @DisplayName("Ut.jwt.toString 을 통해서 JWT 생성, {name=\"Paul\", age=23}")
    void t3() {
        String jwt = Ut.jwt.toString(secret, expireSeconds, Map.of("name", "Paul", "age", 23));

        assertThat(jwt).isNotBlank();

        System.out.println("jwt = " + jwt);
    }

    @Test
    @DisplayName("authTokenService.genAccessToken(member)")
    void t4() {
        Member memberUser1 = memberService.findByUsername("user1").get();

        String accessToken = authTokenService.genAccessToken(memberUser1);

        assertThat(accessToken).isNotBlank();

        System.out.println("accessToken = " + accessToken);
    }
}