package web.synergy.repository;

import web.synergy.domain.model.MuseumData;
import com.redis.om.spring.repository.RedisDocumentRepository;

public interface MuseumDataRepository extends RedisDocumentRepository<MuseumData, String> {
}
