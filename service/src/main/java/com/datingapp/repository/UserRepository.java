package com.datingapp.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.datingapp.domain.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	@Query("SELECT u FROM UserEntity u WHERE u.username = :username")
	UserEntity getUserByUsername(@Param("username") String username);

	@Query("SELECT u FROM UserEntity u WHERE u.username <> :name " +
			"AND u.gender = :gender AND u.dateOfBirth >= :minDob " +
			"AND u.dateOfBirth <= :maxDob " +
			"ORDER BY " +
			"   CASE " +
			"       WHEN :orderBy = 'created' THEN u.created" +
			"       ELSE u.lastActive " +
			"END DESC ")
	Page<UserEntity> findAllUser(@Param("gender") String gender,
			@Param("minDob") LocalDateTime minDob,
			@Param("maxDob") LocalDateTime maxDob,
			@Param("name") String name,
			@Param("orderBy") String orderBy,
			Pageable pageable);
}
