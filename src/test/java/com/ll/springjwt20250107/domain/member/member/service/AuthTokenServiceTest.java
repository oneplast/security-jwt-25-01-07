package com.ll.springjwt20250107.domain.member.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.ll.springjwt20250107.domain.member.member.entity.Member;
import com.ll.springjwt20250107.util.Ut;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
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
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + 1000L * expireSeconds);

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        Map<String, Object> payload = Map.of(
                "name", "Paul",
                "age", 23
        );

        String jwtStr = Jwts.builder()
                .claims(Map.of(
                        "name", "Paul",
                        "age", 23
                ))
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();

        assertThat(jwtStr).isNotBlank();

        // 키가 유효한지 테스트
        Map<String, Object> parsedPayload = (Map<String, Object>) Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parse(jwtStr)
                .getPayload();

        // 키로부터 payload 를 파싱한 결과가 원래 payload 와 같은지 테스트
        assertThat(parsedPayload)
                .containsAllEntriesOf(payload);
    }

    @Test
    @DisplayName("Ut.jwt.toString 을 통해서 JWT 생성, {name=\"Paul\", age=23}")
    void t3() {
        Map<String, Object> payload = Map.of("name", "Paul", "age", 23);

        String jwtStr = Ut.jwt.toString(secret, expireSeconds, payload);

        assertThat(jwtStr).isNotBlank();

        assertThat(Ut.jwt.isValid(secret, jwtStr)).isTrue();

        Map<String, Object> parsedPayload = Ut.jwt.payload(secret, jwtStr);

        assertThat(parsedPayload)
                .containsAllEntriesOf(payload);
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