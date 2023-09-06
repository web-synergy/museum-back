package baza.trainee.service;


import baza.trainee.domain.dto.ArticleDto;

public interface ArticleService {

    /**
     * Finds an existing article by given id.
     *
     * @param id id to get an existing article.
     * @return {@link ArticleDto} object containing an existing article with its full content.
     */
    ArticleDto findById(String id);
}
