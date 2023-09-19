package baza.trainee.controller;

import baza.trainee.domain.dto.SearchResponse;
import baza.trainee.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static baza.trainee.constants.SearchConstant.BLANK_QUERY_ERROR_MESSAGE;
import static baza.trainee.constants.SearchConstant.MAX_SIZE_QUERY;
import static baza.trainee.constants.SearchConstant.MIN_SIZE_QUERY;
import static baza.trainee.constants.SearchConstant.SIZE_QUERY_ERROR_MESSAGE;

/**
 * The {@code SearchController} class is a Spring MVC REST controller
 * responsible for handling search-related requests and returning
 * result of searching (list of {@link SearchResponse}).
 * It exposes endpoints under the "/api/search" base path.
 * <p>
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
    /**
     * The service responsible for performing search operations.
     */
    private final SearchService searchService;

    /**
     * Performs a search operation based on the provided query string.
     *
     * @param query The search query string to be validated and processed.
     * @return A list of {@link SearchResponse} objects representing
     * the search results.
     */
    @GetMapping("/{query}")
    @Operation(summary = "Perform a search", description = "Performs a search based on the provided query string.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    List<SearchResponse> search(
            @PathVariable("query")
            @Parameter(description = "Search query string")
            @NotBlank(message = BLANK_QUERY_ERROR_MESSAGE)
            @Size(min = MIN_SIZE_QUERY,
                    max = MAX_SIZE_QUERY,
                    message = SIZE_QUERY_ERROR_MESSAGE) final String query
    ) {
        return searchService.search(query);
    }
}
