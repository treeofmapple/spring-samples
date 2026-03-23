package com.tom.samples.ai.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tom.samples.ai.model.Prompt;

@Repository
public interface PromptRepository extends MongoRepository<Prompt, UUID> {

}
