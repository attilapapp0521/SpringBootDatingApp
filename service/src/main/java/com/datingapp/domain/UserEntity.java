package com.datingapp.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.datingapp.enumeration.Roles;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@EqualsAndHashCode
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private LocalDateTime dateOfBirth;
	private String knownAs;
	private LocalDateTime created;
	private LocalDateTime lastActive;
	private String gender;

	private String introduction;
	private String lookingFor;

	private String interests;
	private String city;
	private String country;
	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "roles")
	private List<Roles> roles = new ArrayList<>();
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<ImageEntity> photos;
	@OneToMany(mappedBy = "sourceUser", cascade = CascadeType.ALL)
	private List<UserLikeEntity> sourceUser;
	@OneToMany(mappedBy = "likedUser", cascade = CascadeType.ALL)
	private List<UserLikeEntity> likedUser;
	@OneToMany(mappedBy = "sender")
	private List<MessageEntity> messageSent;
	@OneToMany(mappedBy = "recipient")
	private List<MessageEntity> messageReceived;

}
