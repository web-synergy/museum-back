package baza.trainee.service.impl;

import org.springframework.stereotype.Service;

import baza.trainee.domain.model.Article;
import baza.trainee.exceptions.custom.EntityNotFoundException;
import baza.trainee.repository.ArticleRepository;
import baza.trainee.service.ArticleService;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article findByTitle(String title) {
        // Implement the logic to fetch the article by title from the repository
        return articleRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException(Article.class.getSimpleName(), "with title" + title));
    }
}
