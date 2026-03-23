package com.tom.stripe.payment.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tom.stripe.payment.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id <> :id")
	boolean isEmailTakenByAnotherUser(@Param("email") String email, @Param("id") UUID id);

	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.nickname = :nickname AND u.id <> :id")
	boolean isNicknameTakenByAnotherUser(@Param("nickname") String nickname, @Param("id") UUID id);

}
