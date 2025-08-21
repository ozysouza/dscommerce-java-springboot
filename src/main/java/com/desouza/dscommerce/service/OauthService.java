package com.desouza.dscommerce.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desouza.dscommerce.dto.email.EmailDTO;
import com.desouza.dscommerce.dto.user.NewPasswordDTO;
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

    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void createRecoverToken(EmailDTO emailDTO) {
        User user = userRepository.findByEmail(emailDTO.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("Email not found!");
        }

        String token = UUID.randomUUID().toString();

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(emailDTO.getEmail());
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60));
        entity = passwordRecoverRepository.save(entity);

        String body = """
                <html>
                  <body style="font-family: Arial, sans-serif; color: #333;">
                    <h2>Password Reset Request</h2>
                    <p>Hello,</p>
                    <p>We received a request to reset your password for your <b>DSCommerce</b> account.</p>
                    <p>Please click the button below to set a new password:</p>
                    <p>
                      <a href="%s%s"
                         style="display:inline-block; padding:10px 20px; color:white; background:#007BFF; text-decoration:none; border-radius:5px;">
                         Reset Password
                      </a>
                    </p>
                    <p>This link will expire in <b>%d minutes</b>. If you didn’t request this, just ignore this email.</p>
                    <p>Thank you,<br/>DSCommerce Team</p>
                  </body>
                </html>
                """
                .formatted(recoverUri, token, tokenMinutes);

        emailService.sendEmail(
                emailDTO.getEmail(),
                "Password Reset Request",
                body);
    }

    public void validateSelfOrAdmin(long userId) {
        User me = userService.authenticated();
        if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
            throw new ForbiddenException("Access denied");
        }
    }

    @Transactional
    public void saveNewPassword(NewPasswordDTO newPasswordDTO) {
        List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(
                newPasswordDTO.getToken(),
                Instant.now());

        if (result.size() == 0) {
            throw new ResourceNotFoundException("Token not found");
        }

        User user = userRepository.findByEmail(result.get(0).getEmail());
        user.setPassword(passwordEncoder.encode(newPasswordDTO.getPassword()));
        user = userRepository.save(user);
    }
}
