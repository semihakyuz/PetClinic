package com.petclinic;

import java.util.Arrays;
import java.util.HashSet;

import com.petclinic.domain.Role;
import com.petclinic.domain.User;
import com.petclinic.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PetClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public PetClinicApplication(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;

	}

	@EventListener(ApplicationReadyEvent.class)
	public void createUser() {

		Long userCount = userService.userCount();

		if (userCount < 1L) {

			// User user = new User();

			// user.setUsername("user");
			// user.setPassword(bCryptPasswordEncoder.encode("123"));
			// user.setActive(true);
			// Role userRole = new Role("ROLE_USER");
			// user.setRoles(new HashSet<>(Arrays.asList(userRole)));

			// userService.save(user);

			User admin = new User();

			admin.setUsername("admin");
			admin.setPassword(bCryptPasswordEncoder.encode("123"));
			admin.setActive(true);
			Role adminRole = new Role("ROLE_ADMIN");
			admin.setRoles(new HashSet<>(Arrays.asList(adminRole)));
			userService.save(admin);
		}

	}

}
