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

import static baza.trainee.constants.ArticleModelConstants.GET_BY_ID_URL;
import static baza.trainee.constants.ArticleModelConstants.VALID_ARTICLE_DTO;
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
        when(articleService.findById(VALID_ARTICLE_DTO.id())).thenReturn(VALID_ARTICLE_DTO);

        mockMvc.perform(get(GET_BY_ID_URL, VALID_ARTICLE_DTO.id()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findById_notFoundException() throws Exception {
        var wrongId = "wrongId";

        when(articleService.findById(wrongId)).thenThrow(new EntityNotFoundException("article", "id: " + wrongId));

        mockMvc.perform(get(GET_BY_ID_URL, wrongId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}

