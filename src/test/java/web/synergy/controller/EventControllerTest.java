package web.synergy.controller;

import lombok.SneakyThrows;
import web.synergy.domain.mapper.EventMapper;
import web.synergy.domain.model.Event;
import web.synergy.dto.EventResponse;
import web.synergy.dto.PageEvent;
import web.synergy.security.RootUserInitializer;
import web.synergy.service.ArticleService;
import web.synergy.service.EventService;
import web.synergy.service.MailService;

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

import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @MockBean
    private MailService mailService;

    @Autowired
    private EventMapper eventMapper;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private MuseumDataService museumDataService;

    @Test
    void testGetEvents() throws Exception {
        // given:
        var pageable = Pageable.ofSize(10).withPage(0);
        PageEvent events = new PageEvent();
        events.setPageable(pageable);

        // when:
        when(eventService.getAll(pageable)).thenReturn(events);

        // then:
        mockMvc.perform(get("/api/events")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").isEmpty());
    }

    @Test
    void testGetEvent() throws Exception {
        // given:
        final LocalDate begin = LocalDate.of(2023, 9, 3);
        final LocalDate end = LocalDate.of(2023, 9, 12);
        final String eventId = "32";

        Event event = new Event();
        event.setId(eventId);
        event.setTitle("cool title");
        event.setSummary("shortSumm");
        event.setDescription("shortDesc");
        event.setType(EventResponse.TypeEnum.CONTEST.getValue());
        event.setBanner("/images/image1.jpeg");
        event.setBegin(begin);
        event.setEnd(end);

        var response = eventMapper.toResponse(event);

        // when:
        when(eventService.getById(eventId)).thenReturn(response);

        // then:
        mockMvc.perform(get("/api/events/by-id/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId));
    }

    @Test
    void testGetEventBySlug() throws Exception {
        // given:
        final LocalDate begin = LocalDate.of(2023, 9, 3);
        final LocalDate end = LocalDate.of(2023, 9, 12);

        Event event = new Event();
        event.setId("32");
        event.setTitle("cool title");
        event.setSummary("shortSumm");
        event.setDescription("shortDesc");
        event.setType(EventResponse.TypeEnum.CONTEST.getValue());
        event.setBanner("/images/image1.jpeg");
        event.setBegin(begin);
        event.setEnd(end);
        event.setSlug();

        String slug = event.getSlug();

        var response = eventMapper.toResponse(event);

        // when:
        when(eventService.getBySlug(slug)).thenReturn(response);

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
