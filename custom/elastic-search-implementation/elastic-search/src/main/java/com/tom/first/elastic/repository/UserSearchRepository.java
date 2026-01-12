package com.tom.first.elastic.repository;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.tom.first.elastic.models.user.UserDocument;

@Repository
public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, String> {

	Optional<UserDocument> findByName(String name);
	Optional<UserDocument> findByEmail(String email);
	
}
