package com.desouza.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desouza.dscommerce.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByAuthority(String authority);

}
