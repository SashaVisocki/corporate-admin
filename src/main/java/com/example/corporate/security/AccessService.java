package com.example.corporate.security;

import com.example.corporate.model.Permission;
import com.example.corporate.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("access")
// SOLID O — Open/Closed Principle (Принцип открытости/закрытости)
// Определение: новую возможность желательно добавлять расширением, не изменяя
// уже работающий код, который эту возможность использует.
// В этом проекте: можно написать другую реализацию AccessPolicy для иного
// способа проверки прав; код метода can() останется прежним.
// На защите: "Способ проверки можно расширить отдельным классом".
public class AccessService {
    private final UserRepository users;

    // SOLID D — Dependency Inversion Principle (Принцип инверсии зависимостей)
    // Определение: основной код должен зависеть от общего договора (интерфейса),
    // а не от конкретной реализации этого договора.
    // В этом проекте: поле policy имеет тип AccessPolicy, не RoleAccessPolicy.
    // На защите: "Сервис знает, что право можно проверить, но не знает деталей".
    private final AccessPolicy policy;

    // DEPENDENCY INJECTION — Dependency Injection (Внедрение зависимостей):
    // Определение: нужные объекты передаются классу извне, а не создаются им через new.
    // В этом проекте: Spring передаёт репозиторий и политику доступа в конструктор.
    // На защите: "Spring сам передаёт сервису его зависимости".
    public AccessService(UserRepository users, AccessPolicy policy) { this.users = users; this.policy = policy; }
    public boolean can(Authentication auth, String permission) {
        if (auth == null || !auth.isAuthenticated()) return false;
        try {
            Permission required = Permission.valueOf(permission);
            return users.findByUsername(auth.getName()).map(user -> policy.allows(user, required)).orElse(false);
        } catch (IllegalArgumentException e) { return false; }
    }
}
