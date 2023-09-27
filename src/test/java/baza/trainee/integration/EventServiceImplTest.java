package baza.trainee.integration;

import baza.trainee.domain.dto.event.EventPublication;
import baza.trainee.domain.mapper.EventMapper;
import baza.trainee.domain.model.ContentBlock;
import baza.trainee.domain.model.Event;
import baza.trainee.exceptions.custom.EntityNotFoundException;
import baza.trainee.service.EventService;
import baza.trainee.service.ImageService;
import baza.trainee.service.SearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static baza.trainee.domain.enums.BlockType.PICTURE_BLOCK;
import static org.junit.jupiter.api.Assertions.*;

@Import({EventTestDataInitializer.class})
class EventServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper mapper;

    @MockBean
    private SearchService searchService;

    @MockBean
    private ImageService imageService;

    @Test
    @DisplayName("Checking number of pages and objects found.")
    void getAllTest() {

        // given:
        var pageable = Pageable.ofSize(10).withPage(0);
        Page<Event> result = eventService.getAll(pageable);

        // when:
        int numberOfElements = result.getNumberOfElements();

        // then:
        assertEquals(10, numberOfElements);

        for (var event : result) {
            assertNotNull(event.getId());
            assertNotNull(event.getCreated());
        }
    }

    @Test
    @DisplayName("Checking correctness of the search by id.")
    void getByIdTest() {

        // given:
        ContentBlock cb = new ContentBlock();
        cb.setId("thisId");
        cb.setOrder(14);
        cb.setColumns(5);
        cb.setBlockType(PICTURE_BLOCK);
        cb.setTextContent("thisContent");
        MockHttpSession session = new MockHttpSession(null, "httpSessionId");
        EventPublication eventPublication = new EventPublication(
                "Title1",
                "Description1",
                "Type1",
                null,
                Set.of(cb),
                "event/banner1",
                LocalDate.now(),
                LocalDate.now().plusDays(10));

        // when:
        Event createdEvent = eventService.save(eventPublication, session.getId());

        // then:
        assertFalse(createdEvent.getId().isEmpty());
        assertNotNull(createdEvent.getCreated());
        assertEquals(eventPublication.title(), createdEvent.getTitle());
        assertEquals(eventPublication.description(), createdEvent.getDescription());
        assertEquals(eventPublication.begin(), createdEvent.getBegin());
        assertEquals(eventPublication.end(), createdEvent.getEnd());
    }


    @Test
    @DisplayName("Checking correctness of update object.")
    void updateTest() {

        // given:
        ContentBlock cb = new ContentBlock();
        cb.setId("thisId");
        cb.setOrder(14);
        cb.setColumns(5);
        cb.setBlockType(PICTURE_BLOCK);
        cb.setTextContent("thisContent");
        Set<ContentBlock> contentBlocks = new HashSet<>();
        contentBlocks.add(cb);
        MockHttpSession session = new MockHttpSession(null, "httpSessionId");
        var eventPublication = new EventPublication(
                "Title2",
                "Description2",
                "Type2",
                null,
                contentBlocks,
                "event/banner2",
                LocalDate.now(),
                LocalDate.now().plusDays(10));
        Event eventToUpdate = eventService.save(eventPublication, session.getId());

        var eventPublicationForUpdate = new EventPublication(
                "TitleUpdate",
                "DescriptionUpdate",
                "TypeUpdate",
                null,
                null,
                "event/bannerUpdate",
                LocalDate.now(),
                LocalDate.now().plusDays(10));

        // when:
        String id = eventToUpdate.getId();
        Event expected = mapper.toEvent(eventPublicationForUpdate);
        expected.setId(id);
        Event actual = eventService.update(id, eventPublicationForUpdate);

        // then:
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Checking delete object.")
    void deleteEventByIdTest() {

        // given:
        ContentBlock cb = new ContentBlock();
        cb.setId("thisId");
        cb.setOrder(14);
        cb.setColumns(5);
        cb.setBlockType(PICTURE_BLOCK);
        cb.setTextContent("thisContent");
        Set<ContentBlock> contentBlocks = new HashSet<>();
        contentBlocks.add(cb);
        MockHttpSession session = new MockHttpSession(null, "httpSessionId");
        var eventPublication = new EventPublication(
                "Title3",
                "Description3",
                "Type3",
                null,
                contentBlocks,
                "event/banner3",
                LocalDate.now(),
                LocalDate.now().plusDays(10));

        // when:
        Event eventDelete = eventService.save(eventPublication, session.getId());
        String id = eventDelete.getId();
        eventService.deleteEventById(id);

        // then:
        assertThrows(EntityNotFoundException.class, () -> eventService.getById(id),
                "Event with `ID: " + id + "` was not found!");
    }
}
