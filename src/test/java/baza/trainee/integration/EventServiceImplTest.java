package baza.trainee.integration;

import baza.trainee.domain.dto.event.EventPublication;
import baza.trainee.domain.model.Event;
import baza.trainee.service.EventService;
import baza.trainee.service.SearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;


@Import({ EventTestDataInitializer.class })
class EventServiceImplTest extends AbstractIntegrationTest {

    @MockBean
    private SearchService searchService;

    @Autowired
    private EventService eventService;

    @Test
    @DisplayName("Checking number of pages and objects found.")
    void getAll(){
        var pageable = Pageable.ofSize(10).withPage(0);
        Page<Event> result = eventService.getAll(pageable);
        int numberPage = result.getTotalPages();
        int numberEvents = (int)result.getTotalElements();
        Assertions.assertEquals(2, numberPage);
        Assertions.assertEquals(20, numberEvents);
    }

    @Test
    @DisplayName("Checking correctness of the search by id.")
    void getById() {
        EventPublication eventPublication = new EventPublication("Title1","Description1","Type1",null,null,"event/banner1",LocalDate.ofEpochDay(2023-5-25),LocalDate.ofEpochDay(2023-5-30));
        Event newEvent = eventService.save(eventPublication);
        Event checkEvent = eventService.getById(newEvent.getId());
        Assertions.assertEquals(newEvent, checkEvent);
    }

//    @Test
//    void save() {
//    }

    @Test
    @DisplayName("Checking correctness of update object.")
    void update() {
        EventPublication eventPublication = new EventPublication("Title2","Description2","Type2",null,null,"event/banner2",LocalDate.ofEpochDay(2023-6-25),LocalDate.ofEpochDay(2023-6-30));
        Event eventUpdate = eventService.save(eventPublication);
        EventPublication eventPublicationUpdate = new EventPublication("TitleUpdate","DescriptionUpdate","TypeUpdate",null, null, "event/bannerUpdate",LocalDate.ofEpochDay(2023-6-15),LocalDate.ofEpochDay(2023-6-20));
        String id = eventUpdate.getId();
        eventService.update(id, eventPublicationUpdate);
        Event checkEvent = eventService.getById(eventUpdate.getId());
        Assertions.assertEquals(eventUpdate, checkEvent);
    }

    @Test
    @DisplayName("Checking delete object.")
    void deleteEventById() {
        EventPublication eventPublication = new EventPublication("Title3","Description3","Type3",null,null,"event/banner3",LocalDate.ofEpochDay(2023-6-25),LocalDate.ofEpochDay(2023-6-30));
        Event eventDelete = eventService.save(eventPublication);
        String id = eventDelete.getId();
        eventService.deleteEventById(id);
        Assertions.assertNotNull(eventService.getById(id));
    }
}
