package baza.trainee.integration;

import baza.trainee.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.stream.Stream;

import static baza.trainee.integration.EventTestDataInitializer.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Import({ EventTestDataInitializer.class })
class SearchIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private SearchService searchService;

    @MockBean
    private HttpServletRequest httpServletRequest;

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
        responses.forEach(System.out::println);
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
        responses.forEach(System.out::println);
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
        responses.forEach(System.out::println);
        assertEquals(1, responses.size());
    }

    public static Stream<Arguments> searchByFullSummaryTest() {
        return Arrays.stream(EventSummary.values())
                .map(EventSummary::getValue)
                .map(Arguments::of);
    }
}
