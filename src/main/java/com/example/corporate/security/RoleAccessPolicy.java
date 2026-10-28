package com.example.corporate.security;

import com.example.corporate.model.AppUser;
import com.example.corporate.model.Permission;
import org.springframework.stereotype.Component;

@Component
// STRATEGY — Strategy (Стратегия, поведенческий паттерн)
// Определение: способ выполнения задачи вынесен в отдельный класс за общим
// интерфейсом; реализацию можно заменить другим способом той же задачи.
// В этом проекте: RoleAccessPolicy проверяет доступ по разрешениям роли.
// Это одна реализация AccessPolicy; другую политику можно добавить позже.
// На защите: "Алгоритм проверки прав отделён от места, где его вызывают".

// SOLID L — Liskov Substitution Principle (Принцип подстановки Лисков)
// Определение: реализация должна подходить вместо своего интерфейса и выполнять
// обещанное им поведение, не ломая использующий её код.
// В этом проекте: AccessService ожидает AccessPolicy, а RoleAccessPolicy
// выполняет его договор: метод allows() возвращает ответ о доступе.
// На защите: "Сервису подходит этот класс как реализация AccessPolicy".
public class RoleAccessPolicy implements AccessPolicy {
    @Override public boolean allows(AppUser user, Permission permission) {
        return user.isEnabled() && user.getRole().getPermissions().contains(permission);
    }
}
