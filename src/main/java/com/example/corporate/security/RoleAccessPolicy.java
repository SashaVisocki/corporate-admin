package com.example.corporate.security;

import com.example.corporate.model.AppUser;
import com.example.corporate.model.Permission;
import org.springframework.stereotype.Component;

@Component
// STRATEGY — Strategy (Стратегия, поведенческий паттерн): разные способы выполнения
// одной задачи оформляются отдельными классами за общим интерфейсом.
// В нашем коде: RoleAccessPolicy — способ проверить право по роли пользователя.
// Сейчас способ один; при необходимости можно сделать другую реализацию AccessPolicy.
// SOLID L — Liskov Substitution Principle (Принцип подстановки Лисков):
// реализация должна работать вместо своего интерфейса, не нарушая его договор.
// В нашем коде: AccessService ждёт AccessPolicy, а RoleAccessPolicy реализует
// его метод allows() и возвращает ответ "разрешено" или "запрещено".
// На защите: "Проверка смотрит, активен ли пользователь и есть ли право у роли".
public class RoleAccessPolicy implements AccessPolicy {
    @Override public boolean allows(AppUser user, Permission permission) {
        return user.isEnabled() && user.getRole().getPermissions().contains(permission);
    }
}
