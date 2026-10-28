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
    public static Builder builder(String username, String passwordHash, String fullName, Role role) {
        return new Builder(username, passwordHash, fullName, role);
    }

    // BUILDER — Builder (Строитель, порождающий паттерн)
    // Определение: объект создаётся по шагам; обязательные и дополнительные данные
    // можно задать отдельно, а готовый объект получить вызовом build().
    // В этом проекте: Builder собирает AppUser. Логин, хеш пароля, имя и роль
    // обязательны; отдел и состояние учётной записи задаются при необходимости.
    // На защите: "Строитель помогает понятно создать нового пользователя".
    public static class Builder {
        private final String username;
        private final String passwordHash;
        private final String fullName;
        private final Role role;
        private Department department;
        private boolean enabled = true;
        private Builder(String username, String passwordHash, String fullName, Role role) {
            this.username = username;
            this.passwordHash = passwordHash;
            this.fullName = fullName;
            this.role = role;
        }
        public Builder department(Department department) { this.department = department; return this; }
        public Builder enabled(boolean enabled) { this.enabled = enabled; return this; }
        public AppUser build() {
            AppUser user = new AppUser(username, passwordHash, fullName, role, department);
            user.setEnabled(enabled);
            return user;
        }
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
