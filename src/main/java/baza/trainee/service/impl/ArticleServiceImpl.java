package baza.trainee.service.impl;

import baza.trainee.domain.dto.ArticleDto;
import baza.trainee.service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {
    /**
     * Finds an existing article by given id.
     *
     * @param id id to get an existing article.
     * @return {@link ArticleDto} object containing an existing article with its full content.
     */
    @Override
    public ArticleDto findById(final String id) {
        return null;
    }
}
