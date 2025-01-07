package com.ll.springjwt20250107.global.rq;

import com.ll.springjwt20250107.domain.member.member.entity.Member;
import com.ll.springjwt20250107.domain.member.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

// Request/Response 를 추상화한 객체
// Request, Response, Cookie, Session 등을 다룬다.
@RequestScope
@Component
@RequiredArgsConstructor
public class Rq {
    private final MemberService memberService;

    public Member getActorByUsername(String username) {
        return memberService.findByUsername(username).get();
    }

    // 스프링 시큐리티가 이해하는 방식으로 강제 로그인 처리
    // 임시
    public void setLogin(String username) {
        UserDetails user = new User(username, "", List.of());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Member getActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        if (authentication.getPrincipal() == null || authentication.getPrincipal() instanceof String) {
            return null;
        }

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String username = user.getUsername();

        return memberService.findByUsername(username).get();
    }
}
