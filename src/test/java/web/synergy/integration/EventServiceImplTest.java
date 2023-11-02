package web.synergy.integration;

import org.springframework.data.domain.PageRequest;
import web.synergy.domain.mapper.EventMapper;
import web.synergy.dto.EventPublication;
import web.synergy.exceptions.custom.EntityNotFoundException;
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

    @MockBean
    private ImageService imageService;

    private EventPublication eventPublication;

    @BeforeEach
    void setUp() {
        eventPublication = new EventPublication();
        eventPublication.title("Title");
        eventPublication.summary("Short Description");
        eventPublication.description("Not so short Description, but not to long.");
        eventPublication.type(CREATIVE_EVENING);
        eventPublication.banner(UUID.randomUUID().toString());
        eventPublication.status(DRAFT);
        eventPublication.begin(LocalDate.now());
        eventPublication.end(LocalDate.now().plusDays(10));
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
        var createdEvent = eventService.save(eventPublication, userId);

        // then:
        assertFalse(createdEvent.getId().isEmpty());
        assertEquals(eventPublication.getTitle(), createdEvent.getTitle());
        assertEquals(eventPublication.getDescription(), createdEvent.getDescription());
        assertEquals(eventPublication.getBegin(), createdEvent.getBegin());
        assertEquals(eventPublication.getEnd(), createdEvent.getEnd());
    }


    @Test
    @DisplayName("Checking correctness of update object.")
    void updateTest() {

        // given:
        var userId = "USER_ID";

        // when:
        var eventToUpdate = eventService.save(eventPublication, userId);

        // then:
        eventPublication.title("TitleUpdate");
        eventPublication.description("DescriptionUpdate");
        eventPublication.type(CONTEST);
        eventPublication.banner("event/bannerUpdate");

        // when:
        var eventId = eventToUpdate.getId();
        var event = mapper.toEvent(eventPublication);
        event.setId(eventId);

        var expected = mapper.toResponse(event);
        var actual = eventService.update(eventId, eventPublication, userId);

        // then:
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getBanner(), actual.getBanner());

    }

    @Test
    @DisplayName("Checking delete object.")
    void deleteEventByIdTest() {

        // given:
        var userId = "USER_ID";

        // when:
        var eventToDelete = eventService.save(eventPublication, userId);
        var eventId = eventToDelete.getId();
        eventService.deleteEventById(eventId);

        // then:
        assertThrows(EntityNotFoundException.class, () -> eventService.getById(eventId),
                "Event with `ID: " + eventId + "` was not found!");
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
}
