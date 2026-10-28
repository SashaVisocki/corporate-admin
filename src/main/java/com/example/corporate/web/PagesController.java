package com.example.corporate.web;

import com.example.corporate.model.Permission;
import com.example.corporate.security.AccessService;
import com.example.corporate.service.AdminService;
import com.example.corporate.service.DirectoryFacade;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Set;

@Controller
// SOLID S — Single Responsibility Principle (Принцип единственной ответственности)
// Определение: класс должен отвечать за одну основную задачу.
// В этом проекте: PagesController принимает HTTP-запросы и выбирает страницы;
// правила изменения данных он передаёт AdminService.
// На защите: "Контроллер принимает запрос, но не хранит в себе всю логику".

// MVC — Model-View-Controller (Модель-Представление-Контроллер)
// Определение: данные, отображение и обработка запросов разделены.
// В этом проекте: этот класс — Controller; AppUser, Role и Department — Model;
// HTML-шаблоны Thymeleaf — View.
// На защите: "MVC разделяет данные, страницы и управление ими".
public class PagesController {
    private final DirectoryFacade directory;
    private final AdminService admin;
    private final AccessService access;
    public PagesController(DirectoryFacade directory, AdminService admin, AccessService access) {
        this.directory = directory; this.admin = admin; this.access = access;
    }
    @ModelAttribute void navigation(Model model, Authentication auth) {
        model.addAttribute("canEmployees", access.can(auth, "EMPLOYEE_READ"));
        model.addAttribute("canDepartments", access.can(auth, "DEPARTMENT_READ"));
        model.addAttribute("canRoles", access.can(auth, "ROLE_READ"));
        model.addAttribute("canManage", access.can(auth, "USER_MANAGE"));
        model.addAttribute("canEmployeeWrite", access.can(auth, "EMPLOYEE_WRITE"));
        model.addAttribute("canDepartmentWrite", access.can(auth, "DEPARTMENT_WRITE"));
        model.addAttribute("canRoleWrite", access.can(auth, "ROLE_WRITE"));
    }
    @GetMapping("/login") String login() { return "login"; }
    @GetMapping("/denied") String denied() { return "denied"; }
    @GetMapping("/") String home(Model model, Authentication auth) {
        model.addAttribute("username", auth.getName());
        model.addAttribute("employeeCount", directory.employeeCount());
        model.addAttribute("departmentCount", directory.departmentCount());
        return "home";
    }
    @GetMapping("/employees") @PreAuthorize("@access.can(authentication, 'EMPLOYEE_READ')")
    String employees(Model model) { model.addAttribute("employees", directory.employees()); return "employees"; }
    @GetMapping("/employees/new") @PreAuthorize("@access.can(authentication, 'USER_MANAGE')")
    String newEmployee(Model model) { employeeForm(model, null); return "employee-form"; }
    @GetMapping("/employees/{id}/edit") @PreAuthorize("@access.can(authentication, 'USER_MANAGE')")
    String editEmployee(@PathVariable Long id, Model model) { employeeForm(model, id); return "employee-form"; }
    @GetMapping("/employees/{id}/profile") @PreAuthorize("@access.can(authentication, 'EMPLOYEE_WRITE')")
    String editProfile(@PathVariable Long id, Model model) {
        model.addAttribute("employee", directory.employee(id));
        model.addAttribute("departments", directory.departments());
        return "profile-form";
    }
    @PostMapping("/employees/{id}/profile") @PreAuthorize("@access.can(authentication, 'EMPLOYEE_WRITE')")
    String saveProfile(@PathVariable Long id, @RequestParam String fullName,
                       @RequestParam(required = false) Long departmentId, RedirectAttributes flash) {
        try { admin.updateEmployeeProfile(id, fullName, departmentId); flash.addFlashAttribute("message", "Данные сотрудника сохранены"); }
        catch (IllegalArgumentException e) { flash.addFlashAttribute("error", e.getMessage()); return "redirect:/employees/" + id + "/profile"; }
        return "redirect:/employees";
    }
    private void employeeForm(Model model, Long id) {
        model.addAttribute("employee", id == null ? null : directory.employee(id));
        model.addAttribute("roles", directory.roles()); model.addAttribute("departments", directory.departments());
    }
    @PostMapping("/employees/save") @PreAuthorize("@access.can(authentication, 'USER_MANAGE')")
    String saveEmployee(@RequestParam(required = false) Long id, @RequestParam(required = false) String username,
                        @RequestParam String fullName, @RequestParam(required = false) String password,
                        @RequestParam Long roleId, @RequestParam(required = false) Long departmentId,
                        @RequestParam(defaultValue = "false") boolean enabled, RedirectAttributes flash) {
        try { admin.saveUser(id, username, fullName, password, roleId, departmentId, enabled); flash.addFlashAttribute("message", "Сотрудник сохранён"); }
        catch (IllegalArgumentException e) { flash.addFlashAttribute("error", e.getMessage()); return id == null ? "redirect:/employees/new" : "redirect:/employees/" + id + "/edit"; }
        return "redirect:/employees";
    }
    @GetMapping("/departments") @PreAuthorize("@access.can(authentication, 'DEPARTMENT_READ')")
    String departments(Model model) { model.addAttribute("departments", directory.departments()); return "departments"; }
    @PostMapping("/departments/save") @PreAuthorize("@access.can(authentication, 'DEPARTMENT_WRITE')")
    String saveDepartment(@RequestParam(required = false) Long id, @RequestParam String name, RedirectAttributes flash) {
        try { admin.saveDepartment(id, name); flash.addFlashAttribute("message", "Отдел сохранён"); }
        catch (IllegalArgumentException e) { flash.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/departments";
    }
    @GetMapping("/roles") @PreAuthorize("@access.can(authentication, 'ROLE_READ')")
    String roles(Model model) { model.addAttribute("roles", directory.roles()); model.addAttribute("permissions", Permission.values()); return "roles"; }
    @PostMapping("/roles/{id}/permissions") @PreAuthorize("@access.can(authentication, 'ROLE_WRITE')")
    String savePermissions(@PathVariable Long id, @RequestParam(required = false) Set<Permission> permissions, RedirectAttributes flash) {
        try { admin.savePermissions(id, permissions); flash.addFlashAttribute("message", "Права роли сохранены"); }
        catch (IllegalArgumentException e) { flash.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/roles";
    }
}
