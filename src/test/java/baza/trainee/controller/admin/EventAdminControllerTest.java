package baza.trainee.controller.admin;


import baza.trainee.domain.dto.event.EventPublication;
import baza.trainee.domain.mapper.EventMapper;
import baza.trainee.domain.model.ContentBlock;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
class EventAdminControllerTest {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @MockBean
    private RootUserInitializer rootUserInitializer;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCreateEventStatusIsCreated() throws Exception {
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
        var event = eventMapper.toEvent(eventDto);

        // when:
        when(eventService.save(any(EventPublication.class), anyString())).thenReturn(event);

        // then:
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                        .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithMockUser(roles = {"ADMIN"})
    void testCreateEventStatusBadRequest(String validatedField) throws Exception {
        // given:
        MockHttpSession session = new MockHttpSession(null, "httpSessionId");
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
        when(eventService.save(eventDto, session.getId())).thenReturn(event);

        // then:
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateEvent() throws Exception {
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
                        .put("/api/admin/events/{id}", id)
                        .content(eventDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateEventStatusBadRequest(String validatedField) throws Exception {
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
                        .post("/api/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteEvent() throws Exception {
        String eventId = "12";

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/admin/events/{id}", eventId))
                .andExpect(status().isNoContent());

        verify(eventService, times(1)).deleteEventById(eq(eventId));
    }


    @Test
    void testCreateEventStatusIsUnauthorized() throws Exception {
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

        // then:
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testCreateEventStatusIsUnauthorized(String validatedField) throws Exception {
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

        // then:
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateEventStatusIsUnauthorized() throws Exception {
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
        String eventDtoJson = objectMapper.writeValueAsString(eventRequest);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/admin/events/{id}", id)
                        .content(eventDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testUpdateEventStatusIsUnauthorized(String validatedField) throws Exception {
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

        // then:
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteEventIsUnauthorized() throws Exception {
        String eventId = "12";

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/admin/events/{id}", eventId))
                .andExpect(status().isUnauthorized());

        verify(eventService, times(0)).deleteEventById(eq(eventId));
    }
}
