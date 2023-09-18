package baza.trainee.controller;

import baza.trainee.service.SearchService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(SearchController.class)
class SearchControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    SearchService searchService;

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
