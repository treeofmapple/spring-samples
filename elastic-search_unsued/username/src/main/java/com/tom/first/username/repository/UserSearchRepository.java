package com.tom.first.username.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.tom.first.username.model.UserDocument;

import jakarta.transaction.Transactional;

@Repository
public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, String> {

	boolean existsByName(String name);

	boolean existsByEmail(String email);

	@Modifying
	@Transactional
	void deleteByName(String name);

	@Modifying
	@Transactional
	void deleteByEmail(String email);

}
