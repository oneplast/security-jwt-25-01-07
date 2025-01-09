package com.ll.springjwt20250107.global.security;

import com.ll.springjwt20250107.domain.member.member.entity.Member;
import com.ll.springjwt20250107.domain.member.member.service.MemberService;
import com.ll.springjwt20250107.global.rq.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final MemberService memberService;
    private final Rq rq;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = null;
        String accessToken = null;

        String authorization = request.getHeader("Authorization");

        if (authorization != null || authorization.startsWith("Bearer ")) {
            String token = authorization.substring("Bearer ".length());
            String[] tokenBits = token.split(" ", 2);

            if (tokenBits.length == 2) {
                apiKey = tokenBits[0];
                accessToken = tokenBits[1];
            }
        }

        if (apiKey == null || accessToken == null) {
            apiKey = rq.getCookieValue("apiKey");
            accessToken = rq.getCookieValue("accessToken");
        }

        if (apiKey == null || accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Member member = memberService.getMemberFromAccessToken(accessToken);

        if (member == null) {
            Optional<Member> opMemberByApiKey = memberService.findByApiKey(apiKey);

            if (opMemberByApiKey.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            member = opMemberByApiKey.get();

            String newAccessToken = memberService.genAccessToken(member);

            rq.setHeader("Authorization", "Bearer " + apiKey + " " + newAccessToken);
            rq.setCookie("accessToken", newAccessToken);
        }

        rq.setLogin(member);

        filterChain.doFilter(request, response);
    }
}
