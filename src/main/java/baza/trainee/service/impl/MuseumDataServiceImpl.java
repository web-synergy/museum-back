package baza.trainee.service.impl;

import baza.trainee.domain.model.MuseumData;
import baza.trainee.repository.MuseumDataRepository;
import baza.trainee.service.MuseumDataService;
import lombok.RequiredArgsConstructor;

import static baza.trainee.utils.ExceptionUtils.getNotFoundExceptionSupplier;

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
                .orElseThrow(getNotFoundExceptionSupplier(MuseumData.class, "no details"));
    }
}
