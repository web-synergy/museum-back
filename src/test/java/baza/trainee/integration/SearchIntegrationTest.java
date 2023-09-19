package baza.trainee.integration;

import baza.trainee.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Import({ EventTestDataInitializer.class })
class SearchIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private SearchService searchService;

    @Test
    void validQueryTest() {
        var q = "Виставка";

        var searchResponses = searchService.search(q);

        assertFalse(searchResponses.isEmpty());
    }

    @Test
    void minQueryTest() {
        var q = "Вис";

        var responses = searchService.search(q);

        assertFalse(responses.isEmpty());
    }

    @Test
    void byTitleTest() {
        var q = "Виставка архітектурних творінь";

        var responses = searchService.search(q);

        assertEquals(1, responses.size());
    }

    @Test
    void byDescriptionTest() {
        var q = "Запечатліть красу архітектурного силуету міста на фотографіях.";

        var responses = searchService.search(q);

        assertEquals(1, responses.size());
    }

    @Test
    void byContentTest() {
        var q = "Унікальний контент 0 - 0";

        var responses = searchService.search(q);

        assertEquals(1, responses.size());
    }
}
