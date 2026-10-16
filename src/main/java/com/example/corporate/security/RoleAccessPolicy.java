package com.example.corporate.security;

import com.example.corporate.model.AppUser;
import com.example.corporate.model.Permission;
import org.springframework.stereotype.Component;

@Component
public class RoleAccessPolicy implements AccessPolicy {
    @Override public boolean allows(AppUser user, Permission permission) {
        return user.isEnabled() && user.getRole().getPermissions().contains(permission);
    }
}
