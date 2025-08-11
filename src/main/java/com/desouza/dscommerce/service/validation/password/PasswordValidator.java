package com.desouza.dscommerce.service.validation.password;

import java.util.ArrayList;
import java.util.List;

import com.desouza.dscommerce.dto.error.FieldMessageError;
import com.desouza.dscommerce.dto.user.UserInsertDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValid, UserInsertDTO> {

    @Override
    public void initialize(PasswordValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
        List<FieldMessageError> errors = new ArrayList<>();
        String password = dto.getPassword();

        if (password == null || password.isEmpty()) {
            errors.add(new FieldMessageError("password", "Field is required"));
        } else {
            if (password.length() < 8) {
                errors.add(new FieldMessageError("password", "At least 8 characters long"));
            }
            if (!password.matches(".*[a-z].*")) {
                errors.add(new FieldMessageError("password", "At least one lowercase letter"));
            }
            if (!password.matches(".*[A-Z].*")) {
                errors.add(new FieldMessageError("password", "At least one uppercase letter"));
            }
            if (!password.matches(".*\\d.*")) {
                errors.add(new FieldMessageError("password", "At least one digit"));
            }
            if (!password.matches(".*[@$!%*?&].*")) {
                errors.add(new FieldMessageError("password", "At least one special character (@$!%*?&)"));
            }
        }

        if (!errors.isEmpty()) {
            context.disableDefaultConstraintViolation();
            for (FieldMessageError e : errors) {
                context.buildConstraintViolationWithTemplate(e.getMessage())
                        .addPropertyNode(e.getFieldName())
                        .addConstraintViolation();
            }
        }

        return errors.isEmpty();
    }

}
