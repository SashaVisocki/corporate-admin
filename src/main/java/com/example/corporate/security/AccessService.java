package com.example.corporate.security;

import com.example.corporate.model.Permission;
import com.example.corporate.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("access")
// SOLID O — Open/Closed Principle (Принцип открытости/закрытости):
// поведение должно расширяться без изменения уже работающего класса.
// В нашем коде: другой способ проверки прав можно оформить новой реализацией
// AccessPolicy; тогда логику AccessService переписывать не придётся.
// SOLID D — Dependency Inversion Principle (Принцип инверсии зависимостей):
// основной код зависит от абстракции, а не от конкретного класса.
// В нашем коде: поле policy имеет тип AccessPolicy, а не RoleAccessPolicy.
// На защите: "Сервис просит проверить право, не зная деталей проверки".
public class AccessService {
    private final UserRepository users;
    private final AccessPolicy policy;
    // DEPENDENCY INJECTION — Dependency Injection (Внедрение зависимостей):
    // нужные объекты передаются извне, а не создаются внутри класса через new.
    // В нашем коде: Spring передаёт репозиторий и реализацию AccessPolicy в конструктор.
    public AccessService(UserRepository users, AccessPolicy policy) { this.users = users; this.policy = policy; }
    public boolean can(Authentication auth, String permission) {
        if (auth == null || !auth.isAuthenticated()) return false;
        try {
            Permission required = Permission.valueOf(permission);
            return users.findByUsername(auth.getName()).map(user -> policy.allows(user, required)).orElse(false);
        } catch (IllegalArgumentException e) { return false; }
    }
}
