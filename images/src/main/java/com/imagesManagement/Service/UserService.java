package com.imagesManagement.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.imagesManagement.Model.Privilege;
import com.imagesManagement.Model.Role;
import com.imagesManagement.Model.User;
import com.imagesManagement.Repository.PrivilegeRepository;
import com.imagesManagement.Repository.RoleRepository;
import com.imagesManagement.Repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PrivilegeRepository privilegeRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("This user is not exist");
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> {
			role.getPrivileges().forEach(priv -> {
				authorities.add(new SimpleGrantedAuthority(priv.getName()));
			});
			authorities.add(new SimpleGrantedAuthority(role.getName()));

		});
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

	}

	public User save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		addRoleToUser(user.getEmail(), "ROLE_USER");
		return user;
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	public Optional<User> updateUser(User user) {
		Optional<User> existUser = userRepository.findById(user.getId());
		
		if (!existUser.isPresent()) {
			throw new UsernameNotFoundException("This User is doesn't Exist!");
		}
		
		existUser.get().setEmail(user.getEmail());
		existUser.get().setPassword(user.getPassword());
		return existUser;
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}

	public Privilege savePrivilage(Privilege privilege) {
		return privilegeRepository.save(privilege);
	}

	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	public List<Privilege> getAllPrivilage() {
		return privilegeRepository.findAll();
	}

	public Role findRoleByName(String name) {
		Role role = roleRepository.findByName(name);
		if (role == null) {
			throw new IllegalArgumentException("This Role is not exist");
		}
		return role;
	}

	public Privilege findPrivilegeByName(String name) {
		Privilege privilege = privilegeRepository.findByName(name);
		if (privilege == null) {
			throw new IllegalArgumentException("This Privilege is not exist");
		}
		return privilege;
	}

	public void addPrivilegeToRole(String roleName, String privilegeName) {
		Role role = roleRepository.findByName(roleName);
		Privilege privilege = privilegeRepository.findByName(privilegeName);

		if (role == null && privilege == null) {
			throw new IllegalArgumentException("Role Or privilege is not Exist!");
		}
		role.getPrivileges().add(privilege);
	}

	public void addRoleToUser(String userEmail, String roleName) {
		Role role = roleRepository.findByName(roleName);
		User user = userRepository.findByEmail(userEmail);

		if (role == null && user == null) {
			throw new IllegalArgumentException("Role Or User is not Exist!");
		}
		user.getRoles().add(role);
	}

}
