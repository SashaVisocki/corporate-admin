package com.example.corporate;

import com.example.corporate.model.AppUser;
import com.example.corporate.model.Permission;
import com.example.corporate.model.Role;
import com.example.corporate.repository.RoleRepository;
import com.example.corporate.repository.UserRepository;
import com.example.corporate.security.AccessService;
import com.example.corporate.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:access-test;DB_CLOSE_DELAY=-1", "app.admin.password=TestAdminPassword123!"})
class AccessIntegrationTest {
    @Autowired RoleRepository roles;
    @Autowired UserRepository users;
    @Autowired PasswordEncoder encoder;
    @Autowired AccessService access;
    @Autowired AdminService admin;

    @Test void changedRolePermissionsApplyToAnExistingAuthentication() {
        Role viewer = roles.findByName("VIEWER").orElseThrow();
        AppUser user = users.save(new AppUser("viewer-test", encoder.encode("ViewerPassword123!"), "Viewer Test", viewer, null));
        var authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), "", Set.of());
        assertFalse(access.can(authentication, "DEPARTMENT_WRITE"));
        admin.savePermissions(viewer.getId(), Set.of(Permission.EMPLOYEE_READ, Permission.DEPARTMENT_WRITE));
        assertTrue(access.can(authentication, "DEPARTMENT_WRITE"));
        user.setEnabled(false);
        users.save(user);
        assertFalse(access.can(authentication, "DEPARTMENT_WRITE"));
    }

    @Test void protectedAdminRoleCannotBeEdited() {
        Long id = roles.findByName("ADMIN").orElseThrow().getId();
        assertThrows(IllegalArgumentException.class, () -> admin.savePermissions(id, Set.of()));
    }
}
