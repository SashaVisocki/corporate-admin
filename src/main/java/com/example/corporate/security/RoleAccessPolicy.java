package com.example.corporate.security;

import com.example.corporate.model.AppUser;
import com.example.corporate.model.Permission;
import org.springframework.stereotype.Component;

@Component
// STRATEGY (поведенческий) — конкретный способ действия вынесен за общий интерфейс.
// В проекте: это способ проверки доступа по разрешениям роли пользователя.
// SOLID L (подстановка) — реализация должна работать там, где ожидается её интерфейс.
// Здесь RoleAccessPolicy выполняет договор AccessPolicy: отвечает, разрешено ли действие.
// На защите: "проверяю, активен ли пользователь и есть ли нужное право у его роли".
public class RoleAccessPolicy implements AccessPolicy {
    @Override public boolean allows(AppUser user, Permission permission) {
        return user.isEnabled() && user.getRole().getPermissions().contains(permission);
    }
}
