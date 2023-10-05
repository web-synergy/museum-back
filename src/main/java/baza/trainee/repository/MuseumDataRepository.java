package baza.trainee.repository;

import baza.trainee.domain.model.MuseumData;
import com.redis.om.spring.repository.RedisDocumentRepository;

public interface MuseumDataRepository extends RedisDocumentRepository<MuseumData, String> {
}
