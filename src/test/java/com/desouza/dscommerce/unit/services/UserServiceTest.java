package com.desouza.dscommerce.unit.services;

import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.desouza.dscommerce.entities.User;
import com.desouza.dscommerce.projections.UserDetailsProjection;
import com.desouza.dscommerce.repositories.UserRepository;
import com.desouza.dscommerce.services.UserService;
import com.desouza.dscommerce.tests.UserDetailsFactory;
import com.desouza.dscommerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private String validUserName, invalidUserName;
    @SuppressWarnings("unused")
    private User user;
    private List<UserDetailsProjection> userDetails;

    @BeforeEach
    void setUp() throws Exception {
        validUserName = "maria@gmail.com";
        invalidUserName = "test@gmail.com";

        user = UserFactory.createCustomClientUser(1L, validUserName);
        userDetails = UserDetailsFactory.createCustomAdminUser(validUserName);
    }

    @Test
    public void testLoadUserByUsernameShouldReturnUserDetailsWhenUserIsValid() {
        Mockito.when(userRepository.searchUserAndRolesByEmail(validUserName)).thenReturn(userDetails);

        UserDetails result = userService.loadUserByUsername(validUserName);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), validUserName);

        Mockito.verify(userRepository, times(1)).searchUserAndRolesByEmail(validUserName);
    }

    @Test
    public void testLoadUserByUsernameShouldThrowsNotFoundExceptionWhenUserIsInvalid() {
        Mockito.when(userRepository.searchUserAndRolesByEmail(invalidUserName)).thenReturn(new ArrayList<>());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(invalidUserName);
        });
        Mockito.verify(userRepository, times(1)).searchUserAndRolesByEmail(invalidUserName);
    }

}
