package web.synergy.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.domain.PageImpl;
import web.synergy.domain.model.Event;
import web.synergy.dto.EventResponse;
import web.synergy.security.RootUserInitializer;
import web.synergy.service.ArticleService;
import web.synergy.service.EventService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import web.synergy.service.MuseumDataService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Event event;

    @MockBean
    private EventService eventService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private MuseumDataService museumDataService;

    @BeforeEach
    void setUp() {
        final LocalDate begin = LocalDate.of(2023, 9, 3);
        final LocalDate end = LocalDate.of(2023, 9, 12);

        event = new Event();
        event.setId("32");
        event.setTitle("cool title");
        event.setSummary("shortSumm");
        event.setDescription("shortDesc");
        event.setType(EventResponse.TypeEnum.CONTEST.getValue());
        event.setBanner("/images/image1.jpeg");
        event.setBegin(begin);
        event.setEnd(end);
        event.setSlug();
    }

    @Test
    void testGetEvents() throws Exception {
        // given:
        var pageable = Pageable.ofSize(10).withPage(0);
        var events = new PageImpl<Event>(List.of(), pageable, 10L);

        // when:
        when(eventService.getPublished(pageable)).thenReturn(events);

        // then:
        mockMvc.perform(get("/api/events")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").isEmpty());
    }

    @Test
    void testGetEventBySlug() throws Exception {
        // given:
        String slug = event.getSlug();

        // when:
        when(eventService.getBySlug(slug)).thenReturn(event);

        // then:
        mockMvc.perform(get("/api/events/{slug}", slug)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value(slug));
    }

    @SneakyThrows
    @Test
    void testGetEventBySlugWithInvalidSlug() {
        String invalidSlug = "x".repeat(101);

        // then:
        mockMvc.perform(get("/api/events/{slug}", invalidSlug)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
