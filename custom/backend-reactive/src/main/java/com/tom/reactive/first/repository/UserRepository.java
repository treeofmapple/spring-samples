package com.tom.reactive.first.repository;

import java.util.UUID;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.tom.reactive.first.model.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID>, ReactiveQueryByExampleExecutor<User> {

	Mono<User> findByEmailOrNickname(String email, String nickname);

}
