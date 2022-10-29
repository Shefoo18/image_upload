package com.imagesManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imagesManagement.Model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(String name);
}
