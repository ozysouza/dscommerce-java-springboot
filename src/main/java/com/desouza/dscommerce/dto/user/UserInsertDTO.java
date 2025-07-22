package com.desouza.dscommerce.dto.user;

import com.desouza.dscommerce.service.validation.user.UserInsertValid;

import jakarta.validation.constraints.NotBlank;

@UserInsertValid
public class UserInsertDTO extends UserDTO {

    @NotBlank(message = "Fied is required")
    private String password;

    public UserInsertDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
