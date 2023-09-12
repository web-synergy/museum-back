package baza.trainee.controller;

import baza.trainee.exceptions.custom.EntityNotFoundException;
import baza.trainee.service.ArticleService;
import baza.trainee.utils.LoggingService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static baza.trainee.constants.ArticleModelConstants.GET_BY_TITLE_URL;
import static baza.trainee.constants.ArticleModelConstants.VALID_ARTICLE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ArticleService articleService;

    @MockBean
    LoggingService loggingService;

    @Test
    @SneakyThrows
    void findById() {
        when(articleService.findByTitle(VALID_ARTICLE.getTitle())).thenReturn(VALID_ARTICLE);

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

