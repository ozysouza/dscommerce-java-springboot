package com.desouza.dscommerce.tests;

import java.time.LocalDate;

import com.desouza.dscommerce.entities.Role;
import com.desouza.dscommerce.entities.User;

public class UserFactory {

    public static User createClientUser() {
        User user = new User(1L, "Maria", "Brown", "maria@gmail.com", "98956874236", LocalDate.parse("2001-07-25"),
                "$2a$10$hekAig9V0rG/0/Svd5m6..7sl3hQmVhBwqaNmbf9zfEsj6brViBfC");

        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createAdminUser() {
        User user = new User(2L, "Alex", "Santos", "alex@gmail.com", "15796823647", LocalDate.parse("1991-02-03"),
                "$2a$10$hekAig9V0rG/0/Svd5m6..7sl3hQmVhBwqaNmbf9zfEsj6brViBfC");

        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomClientUser(Long id, String username) {
        User user = new User(id, username, "Brown", username, "98956874236", LocalDate.parse("2001-07-25"),
                "$2a$10$hekAig9V0rG/0/Svd5m6..7sl3hQmVhBwqaNmbf9zfEsj6brViBfC");

        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createCustomAdminUser(Long id, String username) {
        User user = new User(id, "Alex", "Santos", username, "15796823647", LocalDate.parse("1991-02-03"),
                "$2a$10$hekAig9V0rG/0/Svd5m6..7sl3hQmVhBwqaNmbf9zfEsj6brViBfC");

        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

}
