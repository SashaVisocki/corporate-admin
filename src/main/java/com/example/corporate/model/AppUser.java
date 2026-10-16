package com.example.corporate.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users")
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 80) private String username;
    @Column(nullable = false) private String passwordHash;
    @Column(nullable = false, length = 100) private String fullName;
    @Column(nullable = false) private boolean enabled = true;
    @ManyToOne(optional = false) private Role role;
    @ManyToOne private Department department;
    protected AppUser() {}
    public AppUser(String username, String passwordHash, String fullName, Role role, Department department) {
        this.username = username; this.passwordHash = passwordHash; this.fullName = fullName;
        this.role = role; this.department = department;
    }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName() { return fullName; }
    public boolean isEnabled() { return enabled; }
    public Role getRole() { return role; }
    public Department getDepartment() { return department; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRole(Role role) { this.role = role; }
    public void setDepartment(Department department) { this.department = department; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
