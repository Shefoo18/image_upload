package com.imagesManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imagesManagement.Model.Privilege;


@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege,Long>{

	Privilege findByName(String name);
}
