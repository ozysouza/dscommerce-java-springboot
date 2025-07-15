package com.desouza.dscommerce.dto.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.desouza.dscommerce.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public class UserDTO {

    private Long id;

    @Size(min = 3, max = 80, message = "Name must be between 3 and 80 characters")
    @NotBlank(message = "Fied is required")
    private String firstName;

    @NotBlank(message = "Fied is required")
    private String lastName;

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Fied is required")
    private String email;

    @NotBlank(message = "Fied is required")
    private String phone;

    @PastOrPresent(message = "Birthdate must be on the past or present")
    @NotBlank(message = "Fied is required")
    private LocalDate birthDate;

    private List<String> roles = new ArrayList<>();

    public UserDTO(Long id, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public UserDTO(User entity) {
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        email = entity.getEmail();
        phone = entity.getPhone();
        birthDate = entity.getBirthDate();
        for (GrantedAuthority role : entity.getRoles()) {
            roles.add(role.getAuthority());
        }
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public List<String> getRoles() {
        return roles;
    }

}
