package com.imagesManagement;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.imagesManagement.Model.Privilege;
import com.imagesManagement.Model.Role;
import com.imagesManagement.Model.User;
import com.imagesManagement.Service.UserService;


@SpringBootApplication
public class ImagesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImagesApplication.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	//@Bean
		CommandLineRunner run(UserService userService) {
			return args -> {
				
				userService.savePrivilage(new Privilege(null,"view"));
				userService.savePrivilage(new Privilege(null,"add"));
				userService.savePrivilage(new Privilege(null,"update"));
				userService.savePrivilage(new Privilege(null,"delete"));
												
				userService.saveRole(new Role(null, "ROLE_USER" , new ArrayList<>() ));
				userService.saveRole(new Role(null, "ROLE_ADMIN", new ArrayList<>()));

				userService.save(new User( null,"user", "user123", new ArrayList<>()));
				userService.save(new User(null,"admin",  "admin123", new ArrayList<>()));

				
				userService.addPrivilegeToRole("ROLE_ADMIN" ,"add");
				userService.addPrivilegeToRole("ROLE_ADMIN" ,"update");
				userService.addPrivilegeToRole("ROLE_ADMIN" ,"view");
				userService.addPrivilegeToRole("ROLE_ADMIN" ,"delete");
				userService.addPrivilegeToRole("ROLE_USER" ,"view");


				userService.addRoleToUser("user", "ROLE_USER" );
				userService.addRoleToUser("admin", "ROLE_ADMIN");



			};
		}

}
