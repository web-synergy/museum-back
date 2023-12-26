package web.synergy.repository;


import java.util.Optional;

import com.redis.om.spring.repository.RedisDocumentRepository;

import web.synergy.domain.model.User;

public interface UserRepository extends RedisDocumentRepository<User, String> {

    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);
    
}
