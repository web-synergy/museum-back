package baza.trainee.service.impl;

import baza.trainee.domain.mapper.ArticleMapper;
import baza.trainee.dto.ArticleResponse;
import lombok.RequiredArgsConstructor;

import static baza.trainee.utils.ExceptionUtils.getNotFoundExceptionSupplier;

import org.springframework.stereotype.Service;

import baza.trainee.domain.model.Article;
import baza.trainee.repository.ArticleRepository;
import baza.trainee.service.ArticleService;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Override
    public ArticleResponse findByTitle(String title) {
        return articleRepository.findByTitle(title)
                .map(articleMapper::toResponse)
                .orElseThrow(getNotFoundExceptionSupplier(Article.class ,"title: " + title));
    }
}
