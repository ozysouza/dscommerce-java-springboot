package com.desouza.dscommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.desouza.dscommerce.config.AppConfig;
import com.desouza.dscommerce.dto.role.RoleDTO;
import com.desouza.dscommerce.dto.user.UserDTO;
import com.desouza.dscommerce.dto.user.UserInsertDTO;
import com.desouza.dscommerce.dto.user.UserUpdateDTO;
import com.desouza.dscommerce.entities.Role;
import com.desouza.dscommerce.entities.User;
import com.desouza.dscommerce.projections.UserDetailsProjection;
import com.desouza.dscommerce.repositories.RoleRepository;
import com.desouza.dscommerce.repositories.UserRepository;
import com.desouza.dscommerce.service.exceptions.DataBaseException;
import com.desouza.dscommerce.service.exceptions.ForbiddenException;
import com.desouza.dscommerce.service.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AppConfig appConfig;

    @Transactional(readOnly = true)
    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");

            return userRepository.searchByEmail(username).get();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource Not Found!");
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Referential integrity constraint violation");
        }
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("There are no users created");
        }
        return users.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        User entity = user.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(entity);
    }

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        User user = authenticated();
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO userInsertDTO) {
        User user = new User();
        copyDtoToEntity(user, userInsertDTO);
        user.setPassword(appConfig.passwordEncoder().encode(userInsertDTO.getPassword()));
        user = userRepository.save(user);
        return new UserDTO(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
        if (result.size() == 0) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection projection : result) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO userUpdateDTO) {
        try {
            Long authenticatedUserId = authenticated().getId();
            if (!authenticatedUserId.equals(id)) {
                throw new ForbiddenException("You are not allowed to update this user");
            }

            User user = userRepository.getReferenceById(id);
            copyDtoToEntity(user, userUpdateDTO);
            user = userRepository.save(user);

            // Force flush to detect constraint violations early
            userRepository.flush();

            return new UserDTO(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource Not Found");
        }
    }

    public void copyDtoToEntity(User user, UserDTO userDTO) {
        user.setBirthDate(userDTO.getBirthDate());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());

        user.getRoles().clear();
        for (RoleDTO roleDTO : userDTO.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            user.getRoles().add(role);
        }
    }

}
