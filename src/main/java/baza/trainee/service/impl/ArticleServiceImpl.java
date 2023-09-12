package baza.trainee.service.impl;

import baza.trainee.domain.model.Article;
import baza.trainee.repository.ArticleRepository;
import baza.trainee.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article findByTitle(String title) {
        // Implement the logic to fetch the article by title from the repository
        return articleRepository.findByTitle(title);
    }
}
