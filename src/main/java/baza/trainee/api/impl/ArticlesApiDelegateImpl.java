package baza.trainee.api.impl;

import baza.trainee.api.ArticlesApiDelegate;
import baza.trainee.dto.ArticleResponse;
import baza.trainee.service.ArticleService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Spring MVC REST controller serving article operations for non-admin users.
 *
 * @author Olha Ryzhkova
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ArticlesApiDelegateImpl implements ArticlesApiDelegate {

    /**
     * The service responsible for performing article operations.
     */
    private final ArticleService articleService;

    /**
     * Finds an existing article by given title.
     *
     * @param title title to get an existing article.
     * @return {@link ArticleResponse} object containing an existing article with its full content.
     */
    @Override
    public ResponseEntity<ArticleResponse> findByTitle(final String title) {
        return new ResponseEntity<>(articleService.findByTitle(title), HttpStatus.OK);
    }
}
