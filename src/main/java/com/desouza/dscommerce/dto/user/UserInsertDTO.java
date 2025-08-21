package com.desouza.dscommerce.dto.user;

import com.desouza.dscommerce.projections.PasswordProjection;
import com.desouza.dscommerce.service.validation.password.PasswordValid;
import com.desouza.dscommerce.service.validation.user.UserInsertValid;

@UserInsertValid
@PasswordValid
public class UserInsertDTO extends UserDTO implements PasswordProjection {

    private String password;

    public UserInsertDTO() {
        super();
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
