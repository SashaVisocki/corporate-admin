package com.example.corporate.repository;

import com.example.corporate.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// REPOSITORY — Repository (Репозиторий): отделяет работу с хранилищем от остального кода.
// В нашем коде: Spring Data JPA через этот интерфейс находит и сохраняет AppUser;
// сервису не нужно писать SQL для этих операций.
// На защите: "Репозиторий — посредник между Java-кодом и базой данных".
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
