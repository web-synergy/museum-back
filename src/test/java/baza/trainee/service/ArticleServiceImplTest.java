package baza.trainee.service;

import baza.trainee.domain.model.Article;
import baza.trainee.exceptions.custom.EntityNotFoundException;
import baza.trainee.repository.ArticleRepository;
import baza.trainee.service.impl.ArticleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testFindByTitle() {
        // Define the expected article
        Article expectedArticle = new Article();
        String title = "Title #1";

        // Mock the behavior of the ArticleRepository.
        when(articleRepository.findByTitle(title)).thenReturn(Optional.of(expectedArticle));

        // Call the service method
        Article resultArticle = articleService.findByTitle(title);

        // Check if the result matches the expected article
        assertEquals(expectedArticle, resultArticle);
    }

    @Test
    void testFindByTitleNotFound() {
        String title = "Non-Existent Title";

        // Mock the behavior of the ArticleRepository to return an empty Optional.
        when(articleRepository.findByTitle(title)).thenReturn(Optional.empty());

        // Call the service method and expect an EntityNotFoundException.
        assertThrows(EntityNotFoundException.class,
                () -> articleService.findByTitle(title), "Article was not found");


    }
}
