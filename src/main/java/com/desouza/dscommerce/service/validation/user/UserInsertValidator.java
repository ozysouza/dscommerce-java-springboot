package com.desouza.dscommerce.service.validation.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.desouza.dscommerce.dto.error.FieldMessageError;
import com.desouza.dscommerce.dto.user.UserInsertDTO;
import com.desouza.dscommerce.entities.User;
import com.desouza.dscommerce.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

		List<FieldMessageError> list = new ArrayList<>();

		User user = userRepository.findByEmail(dto.getEmail());
		if (user != null) {
			list.add(new FieldMessageError("email", "Unique email or primary key violation"));
		}

		for (FieldMessageError e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
