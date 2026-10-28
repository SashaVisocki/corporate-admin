package com.example.corporate.security;

import com.example.corporate.model.AppUser;
import com.example.corporate.model.Permission;

// SOLID I. Подсказка для защиты: "в интерфейсе только нужный метод; реализациям не нужны лишние методы".
public interface AccessPolicy {
    boolean allows(AppUser user, Permission permission);
}
