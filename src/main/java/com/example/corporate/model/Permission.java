package com.example.corporate.model;

public enum Permission {
    EMPLOYEE_READ("Просмотр сотрудников"), EMPLOYEE_WRITE("Изменение сотрудников"),
    DEPARTMENT_READ("Просмотр отделов"), DEPARTMENT_WRITE("Изменение отделов"),
    ROLE_READ("Просмотр ролей"), ROLE_WRITE("Настройка прав"), USER_MANAGE("Управление учетными записями");

    private final String label;
    Permission(String label) { this.label = label; }
    public String getLabel() { return label; }
}
