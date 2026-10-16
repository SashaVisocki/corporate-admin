package com.example.corporate.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_roles")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Enumerated(EnumType.STRING) @Column(name = "permission", nullable = false)
    private Set<Permission> permissions = new HashSet<>();
    protected Role() {}
    public Role(String name, Set<Permission> permissions) { this.name = name; this.permissions = new HashSet<>(permissions); }
    public Long getId() { return id; }
    public String getName() { return name; }
    public Set<Permission> getPermissions() { return permissions; }
    public void setPermissions(Set<Permission> permissions) { this.permissions = new HashSet<>(permissions); }
}
