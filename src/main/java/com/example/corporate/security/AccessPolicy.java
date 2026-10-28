package com.example.corporate.security;

import com.example.corporate.model.AppUser;
import com.example.corporate.model.Permission;

// SOLID I — Interface Segregation Principle (Принцип разделения интерфейсов)
// Определение: интерфейс не должен заставлять класс реализовывать лишние методы.
// В этом проекте: AccessPolicy содержит только allows(), нужный для проверки права.
// RoleAccessPolicy не приходится реализовывать методы, не связанные с доступом.
// На защите: "Интерфейс маленький и требует только то, что нужно проверке прав".
public interface AccessPolicy {
    boolean allows(AppUser user, Permission permission);
}
