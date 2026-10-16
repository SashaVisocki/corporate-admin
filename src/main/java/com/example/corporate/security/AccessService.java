package com.example.corporate.security;

import com.example.corporate.model.Permission;
import com.example.corporate.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("access")
public class AccessService {
    private final UserRepository users;
    private final AccessPolicy policy;
    public AccessService(UserRepository users, AccessPolicy policy) { this.users = users; this.policy = policy; }
    public boolean can(Authentication auth, String permission) {
        if (auth == null || !auth.isAuthenticated()) return false;
        try {
            Permission required = Permission.valueOf(permission);
            return users.findByUsername(auth.getName()).map(user -> policy.allows(user, required)).orElse(false);
        } catch (IllegalArgumentException e) { return false; }
    }
}
