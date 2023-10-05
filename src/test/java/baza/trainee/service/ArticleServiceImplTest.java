package baza.trainee.service;

import baza.trainee.domain.mapper.ArticleMapper;
import baza.trainee.domain.model.Article;
import baza.trainee.dto.ContentBlock;
import baza.trainee.exceptions.custom.EntityNotFoundException;
import baza.trainee.repository.ArticleRepository;
import baza.trainee.security.RootUserInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static baza.trainee.dto.ContentBlock.BlockTypeEnum.TEXT_BLOCK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class ArticleServiceImplTest {

    @MockBean
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleService articleService;

    @MockBean
    private RootUserInitializer rootUserInitializer;


    @Test
    void testFindByTitle() {
        // Define the expected article
        String title = "Title #1";
        var contentBlock = new ContentBlock();
        contentBlock.setId("2312312");
        contentBlock.setBlockType(TEXT_BLOCK);
        contentBlock.setColumns(1);
        contentBlock.setOrder(1);
        contentBlock.setTextContent("Some text content");
        contentBlock.setPictureLink("URL");

        var expectedArticle = new Article();
        expectedArticle.setId("123123");
        expectedArticle.setTitle(title);
        expectedArticle.setDescription("Some valid description");
        expectedArticle.setCreated(LocalDate.now());
        expectedArticle.setContent(Set.of(contentBlock));

        // Mock the behavior of the ArticleRepository.
        when(articleRepository.findByTitle(title)).thenReturn(Optional.of(expectedArticle));

        // Call the service method
        var resultArticle = articleService.findByTitle(title);

        // Check if the result matches the expected article
        assertEquals(articleMapper.toResponse(expectedArticle), resultArticle);
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
