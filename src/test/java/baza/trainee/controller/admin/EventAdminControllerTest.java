package baza.trainee.controller.admin;

import baza.trainee.dto.EventPublication;
import baza.trainee.domain.mapper.EventMapper;
import baza.trainee.security.RootUserInitializer;
import baza.trainee.service.ArticleService;
import baza.trainee.service.EventService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;
import java.util.UUID;

import static baza.trainee.dto.EventPublication.TypeEnum.CONTEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;

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

    @MockBean
    ArticleService articleService;
    
    private final JwtRequestPostProcessor ADMIN_AUTHORITIES = jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"));
    private EventPublication eventDto;
    private String eventDtoJson;
    private MockHttpSession session;
    

    @BeforeEach
    void setUp() throws JsonProcessingException {
        eventDto = new EventPublication();
        eventDto.title("Title");
        eventDto.description("Short Description");
        eventDto.type(CONTEST);
        eventDto.banner(UUID.randomUUID().toString());
        eventDto.summary("Some valid content");
        eventDto.begin(LocalDate.now());
        eventDto.end(LocalDate.now().plusDays(1));

        session = new MockHttpSession(null, "httpSessionId");

        eventDtoJson = objectMapper.writeValueAsString(eventDto);
    }

    @Test
    void testCreateEventStatusIsCreated() throws Exception {
        // given:
        var event = eventMapper.toEvent(eventDto);
        var eventResponse = eventMapper.toResponse(event);

        // when:
        when(eventService.save(any(EventPublication.class), anyString())).thenReturn(eventResponse);

        // then:
        mockMvc.perform(performCreate(eventDtoJson, ADMIN_AUTHORITIES))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testCreateEventStatusBadRequest(String validatedField) throws Exception {
        // given:

        eventDto.title(validatedField);
        eventDto.description(validatedField);
        eventDto.summary(validatedField);

        String invalidEventDtoJson = objectMapper.writeValueAsString(eventDto);
        var event = eventMapper.toEvent(eventDto);
        var eventResponse = eventMapper.toResponse(event);

        // when:
        when(eventService.save(eventDto, session.getId())).thenReturn(eventResponse);

        // then:
        mockMvc.perform(performCreate(invalidEventDtoJson, ADMIN_AUTHORITIES))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateEvent() throws Exception {
        // given:
        String id = "12";
        var event = eventMapper.toEvent(eventDto);
        var eventResponse = eventMapper.toResponse(event);

        String eventDtoJson = objectMapper.writeValueAsString(eventDto);

        // when:
        when(eventService.update(id, eventDto, "httpSessionId")).thenReturn(eventResponse);

        mockMvc.perform(performUpdate(id, eventDtoJson, ADMIN_AUTHORITIES))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testUpdateEventStatusBadRequest(String validatedField) throws Exception {
        // given:
        String id = "12";

        eventDto.title(validatedField);
        eventDto.description(validatedField);
        eventDto.summary(validatedField);

        var event = eventMapper.toEvent(eventDto);
        var eventResponse = eventMapper.toResponse(event);
        String invalidEventDtoJson = objectMapper.writeValueAsString(eventDto);

        // when:
        when(eventService.update(id, eventDto, "httpSessionId")).thenReturn(eventResponse);

        // then:
        mockMvc.perform(performUpdate(id, invalidEventDtoJson, ADMIN_AUTHORITIES))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void testDeleteEvent() throws Exception {
        String eventId = "12";

        mockMvc.perform(performDelete(eventId, ADMIN_AUTHORITIES))
                .andExpect(status().isNoContent());

        verify(eventService, times(1)).deleteEventById(eq(eventId));
    }

    @Test
    void testCreateEventStatusIsUnauthorized() throws Exception {
        // expected:
        mockMvc.perform(performCreate(eventDtoJson, anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateEventStatusIsUnauthorized() throws Exception {
        // given:
        String id = "12";

        // expected:
        mockMvc.perform(performUpdate(id, eventDtoJson, anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteEventIsUnauthorized() throws Exception {
        String eventId = "12";

        mockMvc.perform(performDelete(eventId, anonymous()))
                .andExpect(status().isUnauthorized());

        verify(eventService, times(0)).deleteEventById(eq(eventId));
    }

    private <T extends RequestPostProcessor> MockHttpServletRequestBuilder performCreate(
            String jsonBody,
            T postProcessor) {
        return post("/api/admin/events")
                .with(postProcessor)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody);
    }

    private <T extends RequestPostProcessor> MockHttpServletRequestBuilder performUpdate(
            String id,
            String jsonBody,
            T postProcessor) {
        return put("/api/admin/events/{id}", id)
                .with(postProcessor)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .session(session);
    }

    private <T extends RequestPostProcessor> MockHttpServletRequestBuilder performDelete(
            String eventId,
            T postProcessor) {
        return delete("/api/admin/events/{id}", eventId)
                .with(postProcessor);
    }
}
