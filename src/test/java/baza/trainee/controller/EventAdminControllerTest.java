package baza.trainee.controller;


import baza.trainee.domain.dto.event.EventPublication;
import baza.trainee.domain.mapper.EventMapper;
import baza.trainee.domain.model.ContentBlock;
import baza.trainee.domain.model.Event;
import baza.trainee.service.EventService;
import baza.trainee.utils.LoggingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EventAdminController.class)
public class EventAdminControllerTest {

    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @MockBean
    private LoggingService logService;

    @Test
    public void testCreateEventStatusIsCreated() throws Exception {
        // given:
        var eventDto = new EventPublication(
                "Title",
                "Short Description",
                "PAINTING",
                Set.of("tag1", "tag2"),
                Set.of(new ContentBlock()),
                "http://example.com/banner.jpg",
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );
        String eventDtoJson = objectMapper.writeValueAsString(eventDto);

        // when:
        when(eventService.save(any(EventPublication.class))).thenReturn(any(Event.class));

        // then:
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testCreateEventStatusBadRequest(String validatedField) throws Exception {
        // given:
        var eventDto = new EventPublication(
                validatedField,
                validatedField,
                validatedField,
                Set.of("tag1", "tag2"),
                Set.of(new ContentBlock()),
                "http://example.com/banner.jpg",
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );
        String eventDtoJson = objectMapper.writeValueAsString(eventDto);
        var event = eventMapper.toEvent(eventDto);

        // when:
        when(eventService.save(eventDto)).thenReturn(event);

        // then:
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateEvent() throws Exception {
        // given:
        String id = "12";
        var eventRequest = new EventPublication(
                "Title",
                "Short Description",
                "PAINTING",
                Set.of("tag1", "tag2"),
                Set.of(new ContentBlock()),
                "http://example.com/banner.jpg",
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );
        var event = eventMapper.toEvent(eventRequest);

        String eventDtoJson = objectMapper.writeValueAsString(eventRequest);

        // when:
        when(eventService.update(id, eventRequest)).thenReturn(event);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/admin/events/{id}", id)
                        .content(eventDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testUpdateEventStatusBadRequest(String validatedField) throws Exception {
        // given:
        String id = "12";

        var eventDto = new EventPublication(
                validatedField,
                validatedField,
                validatedField,
                Set.of("tag1", "tag2"),
                Set.of(new ContentBlock()),
                "http://example.com/banner.jpg",
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );
        String eventDtoJson = objectMapper.writeValueAsString(eventDto);
        var event = eventMapper.toEvent(eventDto);

        // when:
        when(eventService.update(id, eventDto)).thenReturn(event);

        // then:
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteEvent() throws Exception {
        String eventId = "12";

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/admin/events/{id}", eventId))
                .andExpect(status().isNoContent());

        verify(eventService, times(1)).deleteEventById(eq(eventId));
    }

}
