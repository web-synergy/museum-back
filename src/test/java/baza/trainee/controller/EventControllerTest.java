package baza.trainee.controller;

import baza.trainee.domain.model.Event;
import baza.trainee.exceptions.custom.EntityNotFoundException;
import baza.trainee.exceptions.custom.MethodArgumentNotValidException;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
    RootUserInitializer rootUserInitializer;

    @Test
    void testGetEvents() throws Exception {
        // given:
        var pageable = Pageable.ofSize(10).withPage(0);
        Page<Event> events = Page.empty(pageable);

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

        Event event = new Event(
                eventId,
                "cool title",
                "shortDesc",
                "CINEMA",
                "/images/image1.jpeg",
                "/images/compressed/image1.jpeg",
                begin,
                end);

        // when:
        when(eventService.getById(eventId)).thenReturn(event);

        // then:
        mockMvc.perform(get("/api/events/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId));
    }

    @Test
    void testGetEventWithInvalidId() throws Exception {
        // given:
        final String invalidEventId = "999";
        final String expectedMessage = "Event with `id: 999` was not found!";

        // when:
        when(eventService.getById(invalidEventId)).thenThrow(
                new EntityNotFoundException("Event", "id: " + invalidEventId));

        // then:
        mockMvc.perform(get("/api/events/{id}", invalidEventId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }

    @Test
    void testBadRequest() throws Exception {
        // given:
        String id = "ID";
        String message = "Event not valid!";

        // when:
        when(eventService.getById(id)).thenThrow(
                new MethodArgumentNotValidException(message));

        // then:
        mockMvc.perform(get("/api/events/{id}", id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(message));
    }
}
