package baza.trainee.controller;

import baza.trainee.domain.dto.ArticleDto;
import baza.trainee.domain.mapper.ArticleMapper;
import baza.trainee.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * Map article models within domain and web layers.
     */
    private final ArticleMapper articleMapper;

    /**
     * Finds an existing article by given id.
     *
     * @param articleId id to get an existing article.
     * @return {@link ArticleDto} object containing an existing article with its full content.
     */
    @GetMapping(value = "/{articleId}", produces = "application/json")
    public ArticleDto findById(@PathVariable(name = "articleId") final String articleId) {
        final var articleById = articleService.findById(articleId);
        return articleMapper.toDto(articleById);
    }
}
