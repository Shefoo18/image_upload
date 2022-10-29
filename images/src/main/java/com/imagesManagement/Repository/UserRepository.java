package com.imagesManagement.Repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imagesManagement.Model.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByEmail(String email);

}
