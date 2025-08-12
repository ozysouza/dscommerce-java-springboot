package com.desouza.dscommerce.service.validation.password;

import java.util.ArrayList;
import java.util.List;

import com.desouza.dscommerce.dto.user.UserInsertDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValid, UserInsertDTO> {

    @Override
    public void initialize(PasswordValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
        List<String> errors = new ArrayList<>();
        String password = dto.getPassword();

        if (password == null || password.isEmpty()) {
            errors.add("Field is required");
        } else {
            if (password.length() < 8) {
                errors.add("At least 8 characters long");
            }
            if (!password.matches(".*[a-z].*")) {
                errors.add("At least one lowercase letter");
            }
            if (!password.matches(".*[A-Z].*")) {
                errors.add("At least one uppercase letter");
            }
            if (!password.matches(".*\\d.*")) {
                errors.add("At least one digit");
            }
            if (!password.matches(".*[@$!%*?&].*")) {
                errors.add("At least one special character (@$!%*?&)");
            }
        }

        if (!errors.isEmpty()) {
            context.disableDefaultConstraintViolation();

            String combinedMessage = String.join(" - ", errors);

            context.buildConstraintViolationWithTemplate(combinedMessage)
                    .addPropertyNode("password")
                    .addConstraintViolation();
        }

        return errors.isEmpty();
    }

}
