package baza.trainee.controller;

import baza.trainee.domain.dto.ArticleDto;
import baza.trainee.service.ArticleService;
import baza.trainee.utils.LoggingService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashSet;

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
        //GIVEN
        final var articleDto = new ArticleDto("testId", "testTitle", "testDescription",
                "testContent", new HashSet<>(), LocalDate.now(), null);

        when(articleService.findById(articleDto.id())).thenReturn(articleDto);

        //WHEN
        final var result = mockMvc.perform(get("/api/article/{articleId}", articleDto.id()));

        //THEN
        result
                .andDo(print())
                .andExpect(status().isOk());
    }
}

