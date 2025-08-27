package com.desouza.dscommerce.dto.user;

import com.desouza.dscommerce.projections.PasswordProjection;
import com.desouza.dscommerce.services.validation.password.PasswordValid;

import jakarta.validation.constraints.NotBlank;

@PasswordValid
public class NewPasswordDTO implements PasswordProjection {

    @NotBlank(message = "Field is required")
    private String token;
    private String password;

    public NewPasswordDTO() {
    }

    public NewPasswordDTO(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
