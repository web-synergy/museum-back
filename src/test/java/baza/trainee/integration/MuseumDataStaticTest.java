package baza.trainee.integration;

import baza.trainee.domain.model.MuseumData;
import baza.trainee.repository.MuseumDataRepository;
import baza.trainee.service.impl.MuseumDataServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Import({MuseumDataStaticTestDataInitializer.class})
class MuseumDataStaticTest extends AbstractIntegrationTest {

    @Autowired
    private MuseumDataServiceImpl museumDataService;

    @Autowired
    MuseumDataRepository repository;

    @Test
    void saveStaticArticles() {
        List<MuseumData> result = repository.findAll();
        assertThat(result)
                .isNotNull()
                .isEmpty();

        museumDataService.saveStaticMuseumData();

        result = repository.findAll();
        assertThat(result)
                .isNotNull()
                .hasSize(1);

        assertEquals("(044) 425-33-97", result.get(0).getPhoneNumber());
        assertEquals("kavaleridzemuseum@gmail.com", result.get(0).getEmail());
        assertEquals("до станції «Контрактова площа», далі пройти пішки близько 1 км.",
                result.get(0).getSubwayRoute());
        assertEquals("114; 119; 18ТР.", result.get(0).getBusRoute());
        assertEquals("від станції «Поштова площа» піднятися до Михайлівської площі, далі пройти по вулиці Володимирській до Андріївського узвозу, 21.",
                result.get(0).getFunicularRoute());
    }
}
