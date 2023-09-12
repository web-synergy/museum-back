package baza.trainee.service.impl;

import baza.trainee.domain.model.Event;
import baza.trainee.integration.RedisTestConfig;
import baza.trainee.repository.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import({ RedisTestConfig.class, EventServiceMockConfig.class})
class EventServiceImplTest {

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    private EventServiceImpl eventService;


    private List<Event> myListEvents(){
        Event event1 = new Event("one","Title1","Description1","Type1","event/banner1","event/preview1",LocalDate.ofEpochDay(2023-5-25),LocalDate.ofEpochDay(2023-5-30));
        Event event2 = new Event("two","Title2","Description2","Type2","event/banner2","event/preview2",LocalDate.ofEpochDay(2023-6-25),LocalDate.ofEpochDay(2023-6-30));


//        event1.setId("one");
//        event1.setTitle("Title1");
//        event1.setDescription("Description1");
//        event1.setType("Type1");
//        event1.setBannerURI("event/banner1");
//        event1.setBannerPreviewURI("event/preview1");
//        event1.setBegin(LocalDate.ofEpochDay(2023-5-25));
//        event1.setEnd(LocalDate.ofEpochDay(2023-5-30));
//
//        event2.setId("two");
//        event2.setTitle("Title2");
//        event2.setDescription("Description2");
//        event2.setType("Type2");
//        event2.setBannerURI("event/banner2");
//        event2.setBannerPreviewURI("event/preview2");
//        event2.setBegin(LocalDate.ofEpochDay(2023-6-25));
//        event2.setEnd(LocalDate.ofEpochDay(2023-6-30));

        return List.of(event1, event2);
    }

    @Test
    void getAll() {
        // given:
        var pageable = Pageable.ofSize(10).withPage(0);
        List<Event> events = myListEvents();
        var page = new PageImpl<>(events, pageable, events.size());

        // when:
        when(eventRepository.findAll(pageable)).thenReturn(page);

        // then:
        Page<Event> result = eventService.getAll(pageable);
        int numberEvents = (int)result.getTotalElements();
        int numberPage = result.getTotalPages();
//        Assertions.assertNotNull(numberEvents);           // int is NOT an Object. It can`t be Null.
        Assertions.assertEquals(2, numberEvents);
//        Assertions.assertNotNull(numberPage);             // int is NOT an Object. It can`t be Null.
        Assertions.assertEquals(1, numberPage);
    }

//    @Test
//    void getById() {
//    }
//
//    @Test
//    void save() {
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void deleteEventById() {
//    }
}
