package com.example.corporate.service;

import com.example.corporate.model.*;
import com.example.corporate.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.EnumSet;
import java.util.Set;

@Component
public class BootstrapData implements CommandLineRunner {
    private final RoleRepository roles;
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final String username;
    private final String password;
    public BootstrapData(RoleRepository roles, UserRepository users, PasswordEncoder encoder,
                         @Value("${app.admin.username}") String username,
                         @Value("${app.admin.password}") String password) {
        this.roles = roles; this.users = users; this.encoder = encoder;
        this.username = username; this.password = password;
    }
    @Override public void run(String... args) {
        Role admin = roles.findByName("ADMIN").orElseGet(() -> roles.save(new Role("ADMIN", EnumSet.allOf(Permission.class))));
        roles.findByName("MANAGER").orElseGet(() -> roles.save(new Role("MANAGER", Set.of(
            Permission.EMPLOYEE_READ, Permission.EMPLOYEE_WRITE, Permission.DEPARTMENT_READ))));
        roles.findByName("VIEWER").orElseGet(() -> roles.save(new Role("VIEWER", Set.of(
            Permission.EMPLOYEE_READ, Permission.DEPARTMENT_READ))));
        if (!users.existsByUsername(username)) {
            if (password == null || password.length() < 12) {
                throw new IllegalStateException("Set ADMIN_PASSWORD to at least 12 characters before first run");
            }
            users.save(new AppUser(username, encoder.encode(password), "Administrator", admin, null));
        }
    }
}
