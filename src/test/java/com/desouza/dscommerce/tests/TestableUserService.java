package com.desouza.dscommerce.tests;

import com.desouza.dscommerce.entities.User;
import com.desouza.dscommerce.services.UserService;

public class TestableUserService extends UserService {

    @Override
    public User authenticated() {
        return super.authenticated();
    }
}
