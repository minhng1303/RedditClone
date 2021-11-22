package com.hanu.reddit.security;

import com.hanu.reddit.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class JwtProvider {
    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
//        String token =
        return "asd";
    }
}
