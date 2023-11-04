package web.synergy.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static web.synergy.dto.EventPublication.StatusEnum.DRAFT;
import static web.synergy.dto.EventPublication.TypeEnum.CONTEST;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;

import web.synergy.domain.model.Event;
import web.synergy.service.EventService;

@AutoConfigureMockMvc
public class AdminEventDraftIntegrationTest extends AbstractIntegrationTest {
    private static final String UPDATE_DRAFT_URL = "/api/admin/events/draft/{slug}";
    private static final String CREATE_DRAFT_URL = "/api/admin/events/draft";
    private static final JwtRequestPostProcessor ADMIN_AUTHORITIES = jwt()
            .authorities(new SimpleGrantedAuthority("SCOPE_WRITE"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventService eventService;

    private Event draft;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        draft = new Event();
        draft.setTitle("Title");
        draft.setSummary("Some valid content");
        draft.setDescription("Short Description");
        draft.setType(CONTEST.getValue());
        draft.setStatus(DRAFT.getValue());
        draft.setBegin(LocalDate.now());
        draft.setEnd(LocalDate.now().plusDays(1));
    }

    @Test
    void testCreateDraftEvent_ShouldReturnStatusIsCreated() throws Exception {
        // given:
        var eventDraftJson = String.format("""
                {
                    "title": "%s",
                    "description": "%s",
                    "summary": "%s",
                    "type": "%s",
                    "status": "%s",
                    "begin": "%s",
                    "end": "%s"
                }
                """,
                draft.getTitle(),
                draft.getDescription(),
                draft.getSummary(),
                draft.getType(),
                draft.getStatus(),
                draft.getBegin().toString(),
                draft.getEnd().toString());

        // then:
        mockMvc.perform(performCreate(CREATE_DRAFT_URL, eventDraftJson, ADMIN_AUTHORITIES))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").isNotEmpty());
    }

    @Test
    void testUpdateDraftEvent_ShouldReturnStatusIsOk() throws Exception {
        // given:
        var titleForUpdate = "updated_title";
        var summaryForUpdate = "updated_description";
        var descriptionForUpdate = "updated_description";
        var beginDateForUpdate = LocalDate.now().plusDays(2).toString();
        var endDateForUpdate = LocalDate.now().plusDays(4).toString();

        // when:
        var savedEvent = eventService.save(draft, "root");

        var slug = savedEvent.getSlug();

        var eventDraftJson = String.format("""
                {
                    "slug": "%s",
                    "title": "%s",
                    "description": "%s",
                    "summary": "%s",
                    "type": "%s",
                    "status": "%s",
                    "begin": "%s",
                    "end": "%s"
                }
                """,
                slug,
                titleForUpdate,
                descriptionForUpdate,
                summaryForUpdate,
                draft.getType(),
                draft.getStatus(),
                beginDateForUpdate,
                endDateForUpdate);

        // then:
        mockMvc.perform(performUpdate(UPDATE_DRAFT_URL, slug, eventDraftJson, ADMIN_AUTHORITIES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value(slug))
                .andExpect(jsonPath("$.title").value(titleForUpdate))
                .andExpect(jsonPath("$.description").value(descriptionForUpdate))
                .andExpect(jsonPath("$.summary").value(summaryForUpdate))
                .andExpect(jsonPath("$.begin").value(beginDateForUpdate))
                .andExpect(jsonPath("$.end").value(endDateForUpdate));
    }

    private <T extends RequestPostProcessor> MockHttpServletRequestBuilder performCreate(
            String url,
            String jsonBody,
            T postProcessor) {
        return post(url)
                .with(postProcessor)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON);
    }

    private <T extends RequestPostProcessor> MockHttpServletRequestBuilder performUpdate(
            String url,
            String slug,
            String jsonBody,
            T postProcessor) {
        return put(url, slug)
                .with(postProcessor)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON);
    }
}
