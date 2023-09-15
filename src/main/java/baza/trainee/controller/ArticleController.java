package baza.trainee.controller;

import baza.trainee.domain.model.Article;
import baza.trainee.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static baza.trainee.utils.ControllerUtils.handleFieldsErrors;

/**
 * Spring MVC REST controller serving article operations for non-admin users.
 *
 * @author Olha Ryzhkova
 * @version 1.0
 */
@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    /**
     * The service responsible for performing article operations.
     */
    private final ArticleService articleService;

    /**
     * Finds an existing article by given title.
     *
     * @param title         title to get an existing article.
     * @param bindingResult autowired binding results.
     * @return {@link Article} object containing an existing article with its full content.
     */
    @Operation(summary = "Find article by title", description = "Returns an article by its title.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the article"),
            @ApiResponse(responseCode = "404", description = "Article not found"),
            @ApiResponse(responseCode = "400", description = "Title request is invalid")
    })
    @GetMapping(value = "/{title}", produces = "application/json")
    public Article findByTitle(
            @Parameter(description = "Title of the article")
            @PathVariable(name = "title")
            @NotBlank final String title,
            final BindingResult bindingResult
    ) {
        handleFieldsErrors(bindingResult);

        return articleService.findByTitle(title);
    }
}
