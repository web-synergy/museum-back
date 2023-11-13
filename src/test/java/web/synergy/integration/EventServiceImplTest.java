package web.synergy.integration;

import org.springframework.data.domain.PageRequest;
import web.synergy.domain.mapper.EventMapper;
import web.synergy.domain.model.Event;
import web.synergy.exceptions.custom.EntityNotFoundException;
import web.synergy.repository.EventRepository;
import web.synergy.service.EventService;
import web.synergy.service.ImageService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

import static web.synergy.dto.EventPublication.StatusEnum.DRAFT;
import static web.synergy.dto.EventPublication.TypeEnum.CONTEST;
import static web.synergy.dto.EventPublication.TypeEnum.CREATIVE_EVENING;
import static org.junit.jupiter.api.Assertions.*;

@Import({EventTestDataInitializer.class})
class EventServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper mapper;

    @Autowired
    private EventRepository eventRepository;

    @MockBean
    private ImageService imageService;

    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setTitle("Title");
        event.setSummary("Short Description");
        event.setDescription("Not so short Description, but not to long.");
        event.setType(CREATIVE_EVENING.getValue());
        event.setBanner(UUID.randomUUID().toString());
        event.setStatus(DRAFT.getValue());
        event.setBegin(LocalDate.now());
        event.setEnd(LocalDate.now().plusDays(10));
    }

    @Test
    @DisplayName("Checking number of pages and objects found.")
    void getAllTest() {

        // given:
        var pageable = Pageable.ofSize(10).withPage(0);
        var resultPage = eventService.getAll(pageable);

        // when:
        int numberOfElements = resultPage.getNumberOfElements();

        // then:
        assertEquals(10, numberOfElements);

        for (var event : resultPage.getContent()) {
            assertNotNull(event.getId());
        }
    }

    @Test
    @DisplayName("Checking correctness of the search by id.")
    void getByIdTest() {

        // given:
        var userId = "USER_ID";

        // when:
        var createdEvent = eventService.save(event, userId);

        // then:
        assertFalse(createdEvent.getId().isEmpty());
        assertEquals(event.getTitle(), createdEvent.getTitle());
        assertEquals(event.getDescription(), createdEvent.getDescription());
        assertEquals(event.getBegin(), createdEvent.getBegin());
        assertEquals(event.getEnd(), createdEvent.getEnd());
    }

    @Test
    @DisplayName("Checking correctness of update object.")
    void updateTest() {

        // given:
        var userId = "USER_ID";

        // when:
        var eventToUpdate = eventService.save(event, userId);

        // then:
        event.setTitle("TitleUpdate");
        event.setDescription("DescriptionUpdate");
        event.setType(CONTEST.getValue());
        event.setBanner("event/bannerUpdate");

        // when:
        var slug = eventToUpdate.getSlug();
        event.setSlug(slug);

        var expected = mapper.toResponse(event);
        var actual = eventService.update(slug, event, userId);

        // then:
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getType().getValue(), actual.getType());
        assertEquals(expected.getBanner(), actual.getBanner());
    }

    @Test
    @DisplayName("Checking delete object.")
    void deleteEventBySlugTest() {

        // given:
        var userId = "USER_ID";

        // when:
        var eventToDelete = eventService.save(event, userId);
        var slug = eventToDelete.getSlug();
        eventService.deleteEventBySlug(slug);

        // then:
        assertThrows(EntityNotFoundException.class, () -> eventService.getBySlug(slug),
                "Event with `Slug: " + slug + "` was not found!");
    }

    @Test
    void retrievePublishedEvents() {

        // given:
        var pageable = PageRequest.of(0, 20);

        // when:
        var pagePublishedEvents = eventService.getPublished(pageable);

        // then:
        assertFalse(pagePublishedEvents.getContent().isEmpty());
    }

    @Test
    void retrieveSortedEvents() {

        // given:
        var pageable = PageRequest.of(0, 5);

        // when:
        var pageSortedEvents = eventRepository.findAllSorted(pageable);

        // then:
        assertFalse(pageSortedEvents.getContent().isEmpty());
    }
}
