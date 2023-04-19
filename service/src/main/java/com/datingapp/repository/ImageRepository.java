package com.datingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.datingapp.domain.ImageEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

	@Query("SELECT p FROM ImageEntity p WHERE p.id = :id")
	ImageEntity getById(@Param("id") Long id);
}
