package web.synergy.service;

import web.synergy.domain.model.MuseumData;
import web.synergy.exceptions.custom.EntityNotFoundException;
import web.synergy.repository.MuseumDataRepository;
import web.synergy.security.RootUserInitializer;
import web.synergy.service.impl.MuseumDataServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class MuseumDataServiceTest {

    @Autowired
    private MuseumDataServiceImpl museumDataService;

    @MockBean
    private MuseumDataRepository museumDataRepository;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @MockBean
    ArticleService articleService;

    private MuseumData museumData;

    @BeforeAll
    public void setUp() {
        museumData = new MuseumData();
        museumData.setId("1");
        museumData.setPhoneNumber("123-456-7890");
        museumData.setEmail("example@example.com");
    }

    @Test
    public void testAddMuseumData() {
        when(museumDataRepository.save(any(MuseumData.class))).thenReturn(museumData);

        MuseumData savedData = museumDataService.add(museumData);

        assertEquals(museumData, savedData);
    }

    @Test
    public void testGetData() {
        when(museumDataRepository.findAll()).thenReturn(Collections.singletonList(museumData));

        MuseumData retrievedData = museumDataService.getData();

        assertEquals(museumData, retrievedData);
    }

    @Test
    public void testGetDataIfDataNoPresent() {
        when(museumDataRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> {
            museumDataService.getData();
        });
    }

    @Test
    public void testUpdateMuseumData() {
        when(museumDataRepository.update(any(MuseumData.class))).thenReturn(museumData);

        MuseumData updatedData = museumDataService.update(museumData);

        assertEquals(museumData, updatedData);
    }
}
