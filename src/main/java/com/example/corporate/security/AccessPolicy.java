package com.example.corporate.security;

import com.example.corporate.model.AppUser;
import com.example.corporate.model.Permission;

// SOLID I (разделение интерфейсов) — интерфейс не должен требовать ненужные методы.
// В проекте: для политики доступа нужен только один метод — allows.
// На защите: "класс проверки прав реализует только то, что ему действительно нужно".
public interface AccessPolicy {
    boolean allows(AppUser user, Permission permission);
}
