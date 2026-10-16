package com.example.corporate.security;

import com.example.corporate.model.AppUser;
import com.example.corporate.model.Permission;

public interface AccessPolicy {
    boolean allows(AppUser user, Permission permission);
}
