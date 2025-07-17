package com.desouza.dscommerce.dto.role;

import com.desouza.dscommerce.entities.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoleDTO {

    private Long id;
    @Size(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
    @NotBlank(message = "Fied is required")
    private String authority;

    public RoleDTO() {
    }

    public RoleDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleDTO(Role entity) {
        id = entity.getId();
        authority = entity.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }
}
