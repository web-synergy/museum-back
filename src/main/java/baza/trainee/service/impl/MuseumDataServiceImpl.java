package baza.trainee.service.impl;

import baza.trainee.domain.model.MuseumData;
import baza.trainee.repository.MuseumDataRepository;
import baza.trainee.service.MuseumDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;

import static baza.trainee.utils.ExceptionUtils.getNotFoundExceptionSupplier;

@Service
@RequiredArgsConstructor
public class MuseumDataServiceImpl implements MuseumDataService {

    private final MuseumDataRepository museumDataRepo;
    private final ObjectMapper objectMapper;

    @Value("${static.json.museumData}")
    private String resource;

    @Override
    public MuseumData update(MuseumData museumData) {
        return museumDataRepo.update(museumData);
    }

    @Override
    public MuseumData add(MuseumData museumData) {
        return museumDataRepo.save(museumData);
    }

    @Override
    public MuseumData getData() {
        return museumDataRepo.findAll().stream().findFirst()
                .orElseThrow(getNotFoundExceptionSupplier(MuseumData.class, "no details"));
    }

    @Override
    @PostConstruct
    @Transactional
    public void saveStaticMuseumData() {
        try (InputStream inputStream = new ClassPathResource(resource).getInputStream()) {
            final byte[] fileData = FileCopyUtils.copyToByteArray(inputStream);
            final MuseumData museumData = objectMapper.readValue(fileData, MuseumData.class);
            museumDataRepo.save(museumData);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
