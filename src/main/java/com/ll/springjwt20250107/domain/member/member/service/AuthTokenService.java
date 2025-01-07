package com.ll.springjwt20250107.domain.member.member.service;

import com.ll.springjwt20250107.domain.member.member.entity.Member;
import com.ll.springjwt20250107.util.Ut;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenService {
    public String genAccessToken(Member member) {
        long id = member.getId();
        String username = member.getUsername();

        return Ut.jwt.toString(
                "askdfh12412sdalkghsdaklghsdaklghsadilghewioo12i4o21498ht98ag0m1t099omaopfm-012k4epo",
                60 * 30 * 24 * 365,
                Map.of("id", id, "username", username)
        );
    }
}
