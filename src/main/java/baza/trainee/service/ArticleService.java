package baza.trainee.service;

import baza.trainee.domain.model.Article;

import java.util.List;

public interface ArticleService {

    /**
     * Saves static articles' content to DB while starting the application.
     * @return List of saved static articles.
     */
    List<Article> saveStaticArticles();
}
