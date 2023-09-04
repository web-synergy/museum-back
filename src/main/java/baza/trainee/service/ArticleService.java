package baza.trainee.service;


import baza.trainee.domain.model.Article;

public interface ArticleService {

    /**
     * Finds an existing article by given id.
     *
     * @param id id to get an existing article.
     * @return {@link Article} object containing an existing article with its full content.
     */
    Article findById(String id);
}
