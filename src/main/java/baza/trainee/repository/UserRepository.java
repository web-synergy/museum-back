package baza.trainee.repository;


import java.util.Optional;

import com.redis.om.spring.repository.RedisDocumentRepository;

import baza.trainee.domain.model.User;

public interface UserRepository extends RedisDocumentRepository<User, String> {

    Optional<User> findByEmail(String email);

}
