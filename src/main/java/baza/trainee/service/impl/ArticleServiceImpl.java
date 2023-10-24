package baza.trainee.service.impl;

import baza.trainee.domain.model.Article;
import baza.trainee.repository.ArticleRepository;
import baza.trainee.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ObjectMapper objectMapper;

    @Value("${static.json.articles}")
    private String resource;

    @Override
    @PostConstruct
    @Transactional
    public void saveStaticArticles() {
        try (InputStream inputStream = new ClassPathResource(resource).getInputStream()) {
            final byte[] fileData = FileCopyUtils.copyToByteArray(inputStream);
            final CollectionType collectionType =
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Article.class);
            final List<Article> articleList = objectMapper.readValue(fileData, collectionType);
            articleList.forEach(articleRepository::save);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
