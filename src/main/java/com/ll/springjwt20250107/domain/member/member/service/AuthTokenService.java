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
                "askdfhsdalkghsdaklghsdaklghsadilghewioo12i4o21498ht98ag0m1t099omaopfm-012k4epo",
                60 * 60 * 24 * 365,
                Map.of("id", id, "username", username)
        );
    }

    public Map<String, Object> payload(String secret, String accessToken) {
        Map<String, Object> parsedPayload = Ut.jwt.payload(secret, accessToken);

        if (parsedPayload == null) {
            return null;
        }

        long id = (long) (Integer) parsedPayload.get("id");
        String username = (String) parsedPayload.get("username");

        return Map.of("id", id, "username", username);
    }
}
