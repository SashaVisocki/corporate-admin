package com.example.corporate.repository;

import com.example.corporate.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// REPOSITORY — Repository (Репозиторий)
// Определение: отдельный объект скрывает операции хранения и поиска данных.
// В этом проекте: UserRepository через Spring Data JPA ищет и сохраняет AppUser;
// сервис обращается к его методам, не записывая SQL-запросы вручную.
// На защите: "Репозиторий связывает логику приложения с базой данных".
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
