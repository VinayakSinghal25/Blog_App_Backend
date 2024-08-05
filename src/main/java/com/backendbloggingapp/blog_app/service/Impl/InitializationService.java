package com.backendbloggingapp.blog_app.service.Impl;

import com.backendbloggingapp.blog_app.config.AppConstants;
import com.backendbloggingapp.blog_app.models.Role;
import com.backendbloggingapp.blog_app.models.User;
import com.backendbloggingapp.blog_app.payloads.UserDto;
import com.backendbloggingapp.blog_app.repository.RoleRepository;
import com.backendbloggingapp.blog_app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class InitializationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void initializeRolesAndAdminUser() {

        try {
            // Build the user object using the builder pattern
            UserDto adminUserDto = UserDto.builder()
                    .name("Admin Service")
                    .email("adminservice@email.com")
                    .about("Admin user for the service")
                    .password(passwordEncoder.encode("admin-service"))
                    .build();

            // Map UserDto to User
            User adminUser = this.modelMapper.map(adminUserDto, User.class);
            Role role = this.roleRepository.findById(AppConstants.ADMIN_USER).get();
            adminUser.getRoles().add(role);

            // Save the user to the database
            userRepository.save(adminUser);

            System.out.println("Admin user created with roles: " + adminUser.getRoles());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


