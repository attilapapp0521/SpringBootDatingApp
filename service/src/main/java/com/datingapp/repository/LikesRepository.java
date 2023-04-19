package com.datingapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.datingapp.domain.UserEntity;
import com.datingapp.domain.UserLikeEntity;

@Repository
public interface LikesRepository extends JpaRepository<UserLikeEntity, Long> {

	@Query("SELECT l.likedUser FROM UserLikeEntity l WHERE l.sourceUser.username = :username " +
			"ORDER BY l.likedUser.username")
	Page<UserEntity> getUserLikes(@Param("username") String username,
			Pageable pageable);

	@Query("SELECT l.sourceUser FROM UserLikeEntity l WHERE l.likedUser.username = :username " +
			"ORDER BY l.sourceUser.username")
	Page<UserEntity> getUserLikesBy(@Param("username") String username,
			Pageable pageable);

	@Query("SELECT l FROM UserLikeEntity l WHERE l.sourceUser.id = :sourceId" +
			" AND l.likedUser.id = :likedId")
	UserLikeEntity isLikedUser(@Param("sourceId") Long sourceId,
			@Param("likedId") Long likedId);
}
