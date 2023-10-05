package baza.trainee.controller;

import baza.trainee.domain.mapper.ArticleMapper;
import baza.trainee.exceptions.custom.EntityNotFoundException;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static baza.trainee.constants.ArticleModelConstants.GET_BY_TITLE_URL;
import static baza.trainee.constants.ArticleModelConstants.VALID_ARTICLE;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ArticleMapper articleMapper;

    @MockBean
    ArticleService articleService;

    @MockBean
    RootUserInitializer rootUserInitializer;

    @Test
    void findById() throws Exception {
        when(articleService.findByTitle(VALID_ARTICLE.getTitle())).thenReturn(articleMapper.toResponse(VALID_ARTICLE));

        mockMvc.perform(get(GET_BY_TITLE_URL, VALID_ARTICLE.getTitle()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findById_notFoundException() throws Exception {
        var wrongTitle = "wrongTitle";

        when(articleService.findByTitle(wrongTitle)).thenThrow(new EntityNotFoundException("article", "title: " + wrongTitle));

        mockMvc.perform(get(GET_BY_TITLE_URL, wrongTitle))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}

