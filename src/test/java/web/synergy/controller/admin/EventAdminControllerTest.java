package web.synergy.controller.admin;

import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import web.synergy.domain.model.Event;
import web.synergy.dto.EventDraft;
import web.synergy.dto.EventPublication;
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
import static web.synergy.dto.EventPublication.StatusEnum.DRAFT;
import static web.synergy.dto.EventPublication.TypeEnum.CONTEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    private static final String CREATE_EVENT_URL = "/api/admin/events";
    private static final String CREATE_DRAFT_URL = "/api/admin/events/draft";
    private static final String UPDATE_DRAFT_URL = "/api/admin/events/draft/{slug}";
    private static final String UPDATE_EVENT_URL =  "/api/admin/events/{id}";

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
    private Event event;
    private String eventPublicationJson;
    private String eventDraftJson;
    private MockHttpSession session;


    @BeforeEach
    void setUp() throws JsonProcessingException {
        event = new Event();
        event.setTitle("Title");
        event.setSummary("Some valid content");
        event.setDescription("Short Description");
        event.setType(CONTEST.getValue());
        event.setStatus(DRAFT.getValue());
        event.setBanner(UUID.randomUUID().toString());
        event.setBegin(LocalDate.now());
        event.setEnd(LocalDate.now().plusDays(1));
        event.updateSlug();

        session = new MockHttpSession(null, "httpSessionId");

        var eventPub = new EventPublication();
        eventPub.title(event.getTitle());
        eventPub.type(EventPublication.TypeEnum.valueOf(event.getType()));
        eventPub.summary(event.getSummary());
        eventPub.description(event.getDescription());

        eventPublicationJson = objectMapper.writeValueAsString(eventPub);

        var eventDraft = new EventDraft();
        eventDraft.title(event.getTitle());
        eventDraft.slug(event.getSlug());
        eventDraft.type(EventDraft.TypeEnum.valueOf(event.getType()));
        eventDraft.status(EventDraft.StatusEnum.valueOf(event.getStatus()));
        eventDraft.summary(event.getSummary());
        eventDraft.description(event.getDescription());
        eventDraft.banner(event.getBanner());
        eventDraft.begin(event.getBegin());
        eventDraft.end(event.getEnd());
        eventDraft.summary(event.getSummary());

        eventDraftJson = objectMapper.writeValueAsString(eventDraft);
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
    void testCreateDraftEvent_ShouldReturnStatusIsCreated() throws Exception {
        // when:
        when(eventService.save(any(Event.class), anyString())).thenReturn(event);

        // then:
        mockMvc.perform(performCreate(CREATE_DRAFT_URL, eventDraftJson, ADMIN_AUTHORITIES))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateDraftEvent_ShouldReturnStatusIsOk() throws Exception {
        // when:
        when(eventService.getBySlug(event.getSlug())).thenReturn(event);
        when(eventService.update(anyString(), any(Event.class), anyString())).thenReturn(event);

        // then:
        mockMvc.perform(performUpdate(UPDATE_DRAFT_URL, event.getSlug(), eventDraftJson, ADMIN_AUTHORITIES))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateEventStatusIsCreated() throws Exception {
        // when:
        when(eventService.save(any(Event.class), anyString())).thenReturn(event);

        // then:
        mockMvc.perform(performCreate(CREATE_EVENT_URL, eventPublicationJson, ADMIN_AUTHORITIES))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testCreateEventStatusBadRequest(String validatedField) throws Exception {
        // given:

        event.setTitle(validatedField);
        event.setSummary(validatedField);
        event.setDescription(validatedField);

        var invalidEventDtoJson = objectMapper.writeValueAsString(event);

        // when:
        when(eventService.save(event, session.getId())).thenReturn(event);

        // then:
        mockMvc.perform(performCreate(CREATE_EVENT_URL, invalidEventDtoJson, ADMIN_AUTHORITIES))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateEvent() throws Exception {
        // given:
        var id = "12";
        var eventDtoJson = objectMapper.writeValueAsString(event);

        // when:
        when(eventService.update(id, event, "httpSessionId")).thenReturn(event);

        mockMvc.perform(performUpdate(UPDATE_EVENT_URL, id, eventDtoJson, ADMIN_AUTHORITIES))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testUpdateEventStatusBadRequest(String validatedField) throws Exception {
        // given:
        var id = "12";

        event.setTitle(validatedField);
        event.setSummary(validatedField);
        event.setDescription(validatedField);

        var invalidEventDtoJson = objectMapper.writeValueAsString(event);

        // when:
        when(eventService.update(id, event, "httpSessionId")).thenReturn(event);

        // then:
        mockMvc.perform(performUpdate(UPDATE_EVENT_URL, id, invalidEventDtoJson, ADMIN_AUTHORITIES))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteEvent() throws Exception {
        var eventId = "12";

        mockMvc.perform(performDelete(eventId, ADMIN_AUTHORITIES))
                .andExpect(status().isNoContent());

        verify(eventService, times(1)).deleteEventBySlug(eventId);
    }

    @Test
    void testCreateEventStatusIsUnauthorized() throws Exception {
        // expected:
        mockMvc.perform(performCreate(CREATE_EVENT_URL, eventPublicationJson, anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateEventStatusIsUnauthorized() throws Exception {
        // given:
        var id = "12";

        // expected:
        mockMvc.perform(performUpdate(UPDATE_EVENT_URL, id, eventPublicationJson, anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteEventIsUnauthorized() throws Exception {
        var eventId = "12";

        mockMvc.perform(performDelete(eventId, anonymous()))
                .andExpect(status().isUnauthorized());

        verify(eventService, times(0)).deleteEventBySlug(eventId);
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
            String url,
            String jsonBody,
            T postProcessor) {
        return post(url)
                .with(postProcessor)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody);
    }

    private <T extends RequestPostProcessor> MockHttpServletRequestBuilder performUpdate(
            String url,
            String id,
            String jsonBody,
            T postProcessor) {
        return put(url, id)
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
