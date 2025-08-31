package com.desouza.dscommerce.tests;

import java.util.List;

import org.junit.jupiter.api.Assertions;

import com.desouza.dscommerce.dto.role.RoleDTO;
import com.desouza.dscommerce.dto.user.UserDTO;
import com.desouza.dscommerce.entities.Role;
import com.desouza.dscommerce.entities.User;

public class UserAssertions {

    public static void assertDtoEquals(User actual, UserDTO expected) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(expected.getLastName(), actual.getLastName());
        Assertions.assertEquals(expected.getPhone(), actual.getPhone());
        Assertions.assertEquals(expected.getBirthDate(), actual.getBirthDate());


        List<String> expectedNames = expected.getRoles().stream().map(RoleDTO::getAuthority).toList();
        List<String> actualNames = actual.getRoles().stream().map(Role::getAuthority).toList();

        Assertions.assertEquals(expectedNames, actualNames);
    }

}
