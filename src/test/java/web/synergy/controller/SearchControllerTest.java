package web.synergy.controller;

import web.synergy.security.RootUserInitializer;
import web.synergy.service.ArticleService;
import web.synergy.service.SearchService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class SearchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SearchService searchService;

    @MockBean
    RootUserInitializer rootUserInitializer;

    @MockBean
    ArticleService articleService;

    @Test
    @SneakyThrows
    @DisplayName("should response status 200 when user inputted correct search query")
    void shouldResponseStatus200_whenUserInputtedCorrectSearchQuery() {
        // Given
        String query = "test";

        when(searchService.search(anyString())).thenReturn(new ArrayList<>());

        // When
        var result = mockMvc.perform(get("/api/search/{query}", query));

        // Then
        result
                .andDo(print())
                .andExpect(status().isOk());
    }
}
