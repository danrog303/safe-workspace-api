package com.github.danrog303.safetyapi.services.security;

import com.github.danrog303.safetyapi.data.client.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {
    public UserInfo getAuthenticatedUserInfo() {
        return (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
