package com.datingapp.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.datingapp.domain.MessageEntity;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

	@Query("SELECT m FROM MessageEntity m WHERE m.recipient.username = :username " +
			"AND m.recipientDelete = false ORDER BY m.messageSent DESC ")
	Page<MessageEntity> getMessagesForUserInbox(@Param("username") String username,
			Pageable pageable);

	@Query("SELECT m FROM MessageEntity m WHERE m.sender.username = :username " +
			"AND m.senderDeleted = false ORDER BY m.messageSent DESC")
	Page<MessageEntity> getMessagesForUserOutbox(@Param("username") String username,
			Pageable pageable);

	@Query("SELECT m FROM MessageEntity m WHERE m.recipient.username = :currentUsername " +
			"   AND m.sender.username = :recipientUsername" +
			"   AND m.recipientDeleted = false " +
			"OR m.recipient.username = :recipientUsername " +
			"   AND m.sender.username = :currentUsername " +
			"   AND m.senderDeleted = false " +
			"ORDER BY m.messageSent")
	List<MessageEntity> getMessageThread(@Param("currentUsername") String currentUsername,
			@Param("recipientUsername") String recipientUsername);

	@Query("SELECT m FROM MessageEntity m WHERE m.recipient.username = :username " +
			"AND m.dateRead IS NULL")
	Page<MessageEntity> getMessagesForUserUnread(@Param("username") String username,
			Pageable pageable);

	@Query("SELECT m FROM MessageEntity m WHERE m.id = :id")
	MessageEntity getById(@Param("id") Long id);
}
