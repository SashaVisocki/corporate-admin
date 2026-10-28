package com.example.corporate.service;

import com.example.corporate.model.AppUser;
import com.example.corporate.model.Department;
import com.example.corporate.model.Role;
import com.example.corporate.repository.DepartmentRepository;
import com.example.corporate.repository.RoleRepository;
import com.example.corporate.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
// FACADE — Facade (Фасад, структурный паттерн): даёт один простой вход к нескольким
// внутренним компонентам и скрывает подробности их использования.
// В нашем коде: PagesController обращается к DirectoryFacade, который сам берёт
// сотрудников, отделы и роли из трёх репозиториев.
// На защите: "Контроллер просит данные у фасада, а фасад знает, где их взять".
public class DirectoryFacade {
    private final UserRepository users;
    private final DepartmentRepository departments;
    private final RoleRepository roles;

    public DirectoryFacade(UserRepository users, DepartmentRepository departments, RoleRepository roles) {
        this.users = users;
        this.departments = departments;
        this.roles = roles;
    }

    public long employeeCount() { return users.count(); }
    public long departmentCount() { return departments.count(); }
    public List<AppUser> employees() { return users.findAll(); }
    public AppUser employee(Long id) {
        return users.findById(id).orElseThrow(() -> new IllegalArgumentException("Сотрудник не найден"));
    }
    public List<Department> departments() { return departments.findAll(); }
    public List<Role> roles() { return roles.findAll(); }
}
