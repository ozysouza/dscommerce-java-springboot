package com.desouza.dscommerce.dto.email;

import com.desouza.dscommerce.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDTO {

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Field is required")
    private String email;

    public EmailDTO() {
    }

    public EmailDTO(String email) {
        this.email = email;
    }

    public EmailDTO(User entity) {
        email = entity.getEmail();
    }

    public String getEmail() {
        return email;
    }

}
