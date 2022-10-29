package com.imagesManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.imagesManagement.Model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
	
	@Query("SELECT i FROM Image i WHERE i.status = 1")
	List<Image>findAcceptedImages();
	
	@Query("SELECT i FROM Image i WHERE i.status is null or i.status = 0")
	List<Image>findNonProcessedImages();

}
