package baza.trainee.service.impl;

import baza.trainee.domain.model.MuseumData;
import baza.trainee.exceptions.custom.EntityNotFoundException;
import baza.trainee.repository.MuseumDataRepository;
import baza.trainee.service.MuseumDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MuseumDataServiceImpl implements MuseumDataService {

    private final MuseumDataRepository museumDataRepo;

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
                .orElseThrow(() -> new EntityNotFoundException("MuseumData", "no details"));
    }
}
