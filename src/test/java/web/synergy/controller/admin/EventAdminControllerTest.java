package web.synergy.controller.admin;

import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import web.synergy.domain.model.Event;
import web.synergy.dto.EventPublication;
import web.synergy.domain.mapper.EventMapper;
import web.synergy.security.RootUserInitializer;
import web.synergy.service.ArticleService;
import web.synergy.service.EventService;

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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import web.synergy.service.MuseumDataService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static web.synergy.dto.EventPublication.TypeEnum.CONTEST;
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
    private ArticleService articleService;

    @MockBean
    private MuseumDataService museumDataService;

    private final JwtRequestPostProcessor ADMIN_AUTHORITIES =
            jwt().authorities(new SimpleGrantedAuthority("SCOPE_WRITE"));
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
    void testGetEvents_ShouldReturnStatusOK() throws Exception {
        // given:
        var pageable = Pageable.ofSize(10).withPage(0);
        var events = new PageImpl<Event>(List.of(), pageable, 10);

        // when:
        when(eventService.getAll(pageable)).thenReturn(events);

        // then:
        mockMvc.perform(performGetAll(ADMIN_AUTHORITIES, "10", "0"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
            "'-10',     '0'",
            "'0',       '0'",
            "'10',      '-10'",
            "'',        ''",
            "'String',  'String'"
    })
    void testGetEventsWithInvalidParameters_ShouldReturnStatusBadRequest(String size, String page) throws Exception {

        // then:
        mockMvc.perform(performGetAll(ADMIN_AUTHORITIES, size, page))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateEventStatusIsCreated() throws Exception {
        // given:
        var event = eventMapper.toEvent(eventDto);

        // when:
        when(eventService.save(any(Event.class), anyString())).thenReturn(event);

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

        // when:
        when(eventService.save(event, session.getId())).thenReturn(event);

        // then:
        mockMvc.perform(performCreate(invalidEventDtoJson, ADMIN_AUTHORITIES))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateEvent() throws Exception {
        // given:
        String id = "12";
        var event = eventMapper.toEvent(eventDto);

        String eventDtoJson = objectMapper.writeValueAsString(eventDto);

        // when:
        when(eventService.update(id, event, "httpSessionId")).thenReturn(event);

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
        String invalidEventDtoJson = objectMapper.writeValueAsString(eventDto);

        // when:
        when(eventService.update(id, event, "httpSessionId")).thenReturn(event);

        // then:
        mockMvc.perform(performUpdate(id, invalidEventDtoJson, ADMIN_AUTHORITIES))
                .andExpect(status().isBadRequest());
    }

    @Test
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

    private <T extends RequestPostProcessor> MockHttpServletRequestBuilder performGetAll(
            T postProcessor,
            String size,
            String page
    ) {
        return get("/api/admin/events")
                .param("size", size)
                .param("page", page)
                .with(postProcessor)
                .contentType(MediaType.APPLICATION_JSON);
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
