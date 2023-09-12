package baza.trainee.service;

import baza.trainee.domain.model.Article;
import baza.trainee.repository.ArticleRepository;
import baza.trainee.service.impl.ArticleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Mock the behavior of the ArticleRepository.
        when(articleRepository.findByTitle("Title #1")).thenReturn(new Article());
    }

    @Test
    void testFindByTitle() {
        // Define the expected article
        Article expectedArticle = new Article();

        // Call the service method
        Article resultArticle = articleService.findByTitle("Title #1");

        // Check if the result matches the expected article
        assertEquals(expectedArticle, resultArticle);
    }
}
