package com.desouza.dscommerce.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desouza.dscommerce.dto.email.EmailDTO;
import com.desouza.dscommerce.entities.PasswordRecover;
import com.desouza.dscommerce.entities.User;
import com.desouza.dscommerce.repositories.PasswordRecoverRepository;
import com.desouza.dscommerce.repositories.UserRepository;
import com.desouza.dscommerce.service.exceptions.ForbiddenException;
import com.desouza.dscommerce.service.exceptions.ResourceNotFoundException;

@Service
public class OauthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Transactional
    public void createRecoverToken(EmailDTO emailDTO) {
        User user = userRepository.findByEmail(emailDTO.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("Email not found!");
        }

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(emailDTO.getEmail());
        entity.setToken(UUID.randomUUID().toString());
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60));
        entity = passwordRecoverRepository.save(entity);
    }

    public void validateSelfOrAdmin(long userId) {
        User me = userService.authenticated();
        if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
            throw new ForbiddenException("Access denied");
        }
    }
}
