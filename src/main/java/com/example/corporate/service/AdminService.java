package com.example.corporate.service;

import com.example.corporate.model.*;
import com.example.corporate.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.EnumSet;
import java.util.Set;

@Service
// SOLID S — Single Responsibility Principle (Принцип единственной ответственности):
// у класса должна быть одна основная ответственность и одна причина для изменения.
// В нашем коде: AdminService отвечает за правила изменения данных и их проверку,
// а при изменении вида страниц меняется PagesController, а не этот сервис.
// На защите: "Сервис занимается данными, контроллер — страницами".
public class AdminService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final DepartmentRepository departments;
    private final PasswordEncoder encoder;
    private final String bootstrapUsername;
    public AdminService(UserRepository users, RoleRepository roles, DepartmentRepository departments,
                        PasswordEncoder encoder, @Value("${app.admin.username}") String bootstrapUsername) {
        this.users = users; this.roles = roles; this.departments = departments;
        this.encoder = encoder; this.bootstrapUsername = bootstrapUsername;
    }
    @Transactional
    public void saveUser(Long id, String username, String fullName, String password, Long roleId,
                         Long departmentId, boolean enabled) {
        String cleanName = required(fullName, "Имя", 100);
        Role role = roles.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Роль не найдена"));
        if (role.getName().equals("ADMIN") && (id == null || !users.findById(id).map(u -> u.getUsername().equals(bootstrapUsername)).orElse(false))) {
            throw new IllegalArgumentException("Роль ADMIN закреплена за главным администратором");
        }
        Department department = departmentId == null ? null : departments.findById(departmentId)
            .orElseThrow(() -> new IllegalArgumentException("Отдел не найден"));
        if (id == null) {
            String cleanUsername = required(username, "Логин", 80);
            if (!cleanUsername.matches("[A-Za-z0-9._-]+")) throw new IllegalArgumentException("Логин: только латинские буквы, цифры, точка, дефис и _");
            if (users.existsByUsername(cleanUsername)) throw new IllegalArgumentException("Логин уже занят");
            if (password == null || password.length() < 12) throw new IllegalArgumentException("Пароль должен содержать минимум 12 символов");
            AppUser user = AppUser.builder(cleanUsername, encoder.encode(password), cleanName, role)
                .department(department).enabled(enabled).build();
            users.save(user);
        } else {
            AppUser user = users.findById(id).orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));
            if (user.getUsername().equals(bootstrapUsername) && (!enabled || !role.getName().equals("ADMIN"))) {
                throw new IllegalArgumentException("Главного администратора нельзя отключить или лишить роли ADMIN");
            }
            user.setFullName(cleanName); user.setRole(role); user.setDepartment(department); user.setEnabled(enabled);
            if (password != null && !password.isBlank()) {
                if (password.length() < 12) throw new IllegalArgumentException("Пароль должен содержать минимум 12 символов");
                user.setPasswordHash(encoder.encode(password));
            }
        }
    }
    @Transactional
    public void saveDepartment(Long id, String name) {
        String cleanName = required(name, "Название отдела", 100);
        if (departments.findByNameIgnoreCase(cleanName).filter(existing -> !existing.getId().equals(id)).isPresent())
            throw new IllegalArgumentException("Отдел с таким названием уже существует");
        Department department = id == null ? new Department(cleanName) : departments.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Отдел не найден"));
        department.setName(cleanName);
        departments.save(department);
    }
    @Transactional
    public void updateEmployeeProfile(Long id, String fullName, Long departmentId) {
        AppUser user = users.findById(id).orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));
        user.setFullName(required(fullName, "Имя", 100));
        user.setDepartment(departmentId == null ? null : departments.findById(departmentId)
            .orElseThrow(() -> new IllegalArgumentException("Отдел не найден")));
    }
    @Transactional
    public void savePermissions(Long roleId, Set<Permission> permissions) {
        Role role = roles.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Роль не найдена"));
        if (role.getName().equals("ADMIN")) throw new IllegalArgumentException("Права роли ADMIN зафиксированы для восстановления доступа");
        role.setPermissions(permissions == null || permissions.isEmpty() ? EnumSet.noneOf(Permission.class) : EnumSet.copyOf(permissions));
    }
    private String required(String value, String field, int max) {
        if (value == null || value.isBlank() || value.trim().length() > max)
            throw new IllegalArgumentException(field + ": укажите от 1 до " + max + " символов");
        return value.trim();
    }
}
