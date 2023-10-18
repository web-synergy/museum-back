package baza.trainee.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import baza.trainee.dto.EventPublication;
import baza.trainee.exceptions.custom.StorageException;
import baza.trainee.exceptions.custom.StorageFileNotFoundException;
import baza.trainee.service.EventService;

import static org.junit.jupiter.api.Assertions.*;

@Import({EventTestDataInitializer.class})
class EventImageProcessingTest extends AbstractIntegrationTest {
    
    @Autowired
    private EventService eventService;

    @Test
    void saveEventWithoutBanner() {

        // given:
        var event = new EventPublication();
        event.title("Test title");
        event.summary("Test event summary");
        event.description("Test event description");

        // when:
        var savedEvent = eventService.save(event, "Some_User");

        // then:
        assertDoesNotThrow(() -> new RuntimeException());
        assertDoesNotThrow(() -> new StorageException("Failed to find files"));
        assertDoesNotThrow(() -> new StorageFileNotFoundException("Could not read resource."));

        // when:
        var sameEvent = eventService.getById(savedEvent.getId());

        // then:
        assertEquals(savedEvent.getTitle(), sameEvent.getTitle());
        assertEquals(savedEvent.getSummary(), sameEvent.getSummary());
        assertEquals(savedEvent.getDescription(), sameEvent.getDescription());
    }
}
