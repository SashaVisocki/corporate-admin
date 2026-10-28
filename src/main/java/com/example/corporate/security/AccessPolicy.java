package com.example.corporate.security;

import com.example.corporate.model.AppUser;
import com.example.corporate.model.Permission;

// SOLID I — Interface Segregation Principle (Принцип разделения интерфейсов):
// интерфейс не должен заставлять класс реализовывать ненужные ему методы.
// В нашем коде: AccessPolicy содержит только allows() — проверку одного права.
// На защите: "Здесь нет лишних методов, которые пришлось бы реализовывать".
public interface AccessPolicy {
    boolean allows(AppUser user, Permission permission);
}
