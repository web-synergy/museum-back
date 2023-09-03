package baza.trainee.controller;

import baza.trainee.domain.dto.SearchDto;
import baza.trainee.service.SearchService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The {@code SearchController} class is a Spring MVC REST controller
 * responsible for handling search-related requests and returning
 * result of searching (list of {@link SearchDto}).
 * It exposes endpoints under the "/api/search" base path.
 *
 * This controller ensures that queries meet certain validation criteria and
 * delegates the actual search functionality to the {@link SearchService}.
 *
 * @author Dmytro Teliukov
 * @version 1.0
 * @since 2023-09-03
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Validated
public class SearchController {
    private final SearchService searchService;

    /**
     * Performs a search operation based on the provided query string.
     *
     * @param query The search query string to be validated and processed.
     * @return A list of {@link SearchDto} objects representing
     * the search results.
     */
    @GetMapping("/{query}")
    List<SearchDto> search(@PathVariable("query")
                           @NotBlank(message = "Query should not be blank")
                           @Size(min = 3, max = 120, message = "Size of query should be between 3 and 120 characters")
                           String query) {
        return searchService.search(query);
    }
}
