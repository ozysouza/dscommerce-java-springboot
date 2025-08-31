package com.desouza.dscommerce.unit.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.desouza.dscommerce.entities.User;
import com.desouza.dscommerce.services.OauthService;
import com.desouza.dscommerce.services.exceptions.ForbiddenException;
import com.desouza.dscommerce.tests.TestableUserService;
import com.desouza.dscommerce.tests.factory.UserFactory;

@Tag("unit")
@ExtendWith(SpringExtension.class)
public class OauthServiceTest {

    @InjectMocks
    private OauthService oauthService;

    @Mock
    private TestableUserService testableUserService;

    private User admin, selfClient, otherClient;

    @BeforeEach
    void setUp() throws Exception {
        admin = UserFactory.createAdminUser();
        selfClient = UserFactory.createClientUser();
        otherClient = UserFactory.createCustomClientUser(2L, "Bob");
    }

    @Test
    public void testValidateSelfOrAdminShouldDoNothingWhenAdminLogged() {
        Mockito.when(testableUserService.authenticated()).thenReturn(admin);

        Long userId = admin.getId();

        Assertions.assertDoesNotThrow(() -> {
            oauthService.validateSelfOrAdmin(userId);
        });
    }

    @Test
    public void testValidateSelfOrAdminShouldDoNothingWhenSelfLogged() {
        Mockito.when(testableUserService.authenticated()).thenReturn(selfClient);

        Long userId = selfClient.getId();

        Assertions.assertDoesNotThrow(() -> {
            oauthService.validateSelfOrAdmin(userId);
        });
    }

    @Test
    public void testValidateSelfOrAdminShouldThrowForbiddenExceptionWhenOtherClientLogged() {
        Mockito.when(testableUserService.authenticated()).thenReturn(otherClient);

        Long userId = selfClient.getId();

        Assertions.assertThrows(ForbiddenException.class, () -> {
            oauthService.validateSelfOrAdmin(userId);
        });
    }

}
