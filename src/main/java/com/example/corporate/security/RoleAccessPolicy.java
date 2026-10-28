package com.example.corporate.security;

import com.example.corporate.model.AppUser;
import com.example.corporate.model.Permission;
import org.springframework.stereotype.Component;

@Component
// STRATEGY (поведенческий) / SOLID L: реализация выполняет контракт AccessPolicy.
// Подсказка для защиты: "этот вариант проверки смотрит, активен ли пользователь и есть ли право у его роли".
public class RoleAccessPolicy implements AccessPolicy {
    @Override public boolean allows(AppUser user, Permission permission) {
        return user.isEnabled() && user.getRole().getPermissions().contains(permission);
    }
}
