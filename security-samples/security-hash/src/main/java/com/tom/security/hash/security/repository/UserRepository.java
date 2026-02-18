package com.tom.security.hash.security.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tom.security.hash.security.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

	@Query("SELECT u FROM User u WHERE u.accountNonLocked = true AND u.enabled = true")
	Page<User> findAllActiveUsers(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.id = :id AND u.accountNonLocked = true AND u.enabled = true")
	Optional<User> findActiveUserById(UUID id);
	
	Optional<User> findByEmail(String email);

	Optional<User> findByNickname(String nickname);

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id <> :id")
	boolean isEmailTakenByAnotherUser(@Param("email") String email, @Param("id") UUID id);

	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.nickname = :nickname AND u.id <> :id")
	boolean isNicknameTakenByAnotherUser(@Param("nickname") String nickname, @Param("id") UUID id);

}
