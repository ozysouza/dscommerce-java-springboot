package com.desouza.dscommerce.tests.util;

import org.springframework.test.web.servlet.request.RequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

public class TestUtils {

    public static RequestPostProcessor authorizedUser(String... roles) {
        return user("test").roles(roles);
    }

}
