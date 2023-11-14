package web.synergy.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import web.synergy.integration.EventTestDataInitializer.EventDescription;
import web.synergy.integration.EventTestDataInitializer.EventSummary;
import web.synergy.integration.EventTestDataInitializer.EventTitles;
import web.synergy.service.SearchService;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Import({ 
    EventTestDataInitializer.class})
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

    @ParameterizedTest
    @MethodSource
    void searchByFullDescriptionTest(String result) {
        var responses = searchService.search(result);
        assertEquals(1, responses.size());
    }

    public static Stream<Arguments> searchByFullDescriptionTest() {
        return Arrays.stream(EventDescription.values())
                .map(EventDescription::getValue)
                .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource
    void searchByFullTitleTest(String result) {
        var responses = searchService.search(result);
        assertEquals(1, responses.size());
    }

    public static Stream<Arguments> searchByFullTitleTest() {
        return Arrays.stream(EventTitles.values())
                .map(EventTitles::getValue)
                .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource
    void searchByFullSummaryTest(String result) {
        var responses = searchService.search(result);
        assertEquals(1, responses.size());
    }

    public static Stream<Arguments> searchByFullSummaryTest() {
        return Arrays.stream(EventSummary.values())
                .map(EventSummary::getValue)
                .map(Arguments::of);
    }

    @ParameterizedTest
    @ValueSource(strings = { "Скульптура", "глини", "Кіно", "Іван", "Кавалерідзе", "Сумщині" })
    void searchArticles(String query) {
        var searchResponses = searchService.search(query);

        assertFalse(searchResponses.isEmpty());
    }
}
