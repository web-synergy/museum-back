package web.synergy.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import web.synergy.domain.model.Event;
import web.synergy.exceptions.custom.StorageException;
import web.synergy.exceptions.custom.StorageFileNotFoundException;
import web.synergy.service.EventService;

import static org.junit.jupiter.api.Assertions.*;

@Import({EventTestDataInitializer.class})
class EventImageProcessingTest extends AbstractIntegrationTest {
    
    @Autowired
    private EventService eventService;

    @Test
    void saveEventWithoutBanner() {

        // given:
        var event = new Event();
        event.setTitle("Test title");
        event.setSummary("Test event summary");
        event.setDescription("Test event description");

        // when:
        var savedEvent = eventService.save(event, "Some_User");

        // then:
        assertDoesNotThrow(() -> new RuntimeException());
        assertDoesNotThrow(() -> new StorageException("Failed to find files"));
        assertDoesNotThrow(() -> new StorageFileNotFoundException("Could not read resource."));

        // when:
        var sameEvent = eventService.getBySlug(savedEvent.getSlug());

        // then:
        assertEquals(savedEvent.getTitle(), sameEvent.getTitle());
        assertEquals(savedEvent.getSummary(), sameEvent.getSummary());
        assertEquals(savedEvent.getDescription(), sameEvent.getDescription());
    }
}
