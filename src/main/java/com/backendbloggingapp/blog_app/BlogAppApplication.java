package com.backendbloggingapp.blog_app;

import com.backendbloggingapp.blog_app.config.AppConstants;
import com.backendbloggingapp.blog_app.models.Role;
import com.backendbloggingapp.blog_app.models.User;
import com.backendbloggingapp.blog_app.payloads.UserDto;
import com.backendbloggingapp.blog_app.repository.RoleRepository;
import com.backendbloggingapp.blog_app.repository.UserRepository;
import com.backendbloggingapp.blog_app.service.Impl.InitializationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class BlogAppApplication implements CommandLineRunner {

	@Autowired
	private InitializationService initializationService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;


	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {

		SpringApplication.run(BlogAppApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {

		try {
			//if role is already created, then won't get created,otherwise get created
			Role role = new Role();
			role.setId(AppConstants.ADMIN_USER);
			role.setName("ROLE_ADMIN");

			Role role1 = new Role();
			role1.setId(AppConstants.NORMAL_USER);
			role1.setName("ROLE_NORMAL");

			List<Role> roles = List.of(role, role1);

			List<Role> result = this.roleRepository.saveAll(roles);

			result.forEach(r -> {
				System.out.println(r.getName());
			});

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		initializationService.initializeRolesAndAdminUser();


	}
}
