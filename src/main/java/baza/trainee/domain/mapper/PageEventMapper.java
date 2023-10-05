package baza.trainee.domain.mapper;

import baza.trainee.dto.EventResponse;
import baza.trainee.dto.PageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageEventMapper {

    public PageEvent toPageEvent(Page<EventResponse> page) {
        var pageEvent = new PageEvent();

        pageEvent.setTotalPages(page.getTotalPages());
        pageEvent.setSort(page.getSort());
        pageEvent.setPageable(page.getPageable());
        pageEvent.setNumber(page.getNumber());
        pageEvent.setLast(page.isLast());
        pageEvent.setEmpty(page.isEmpty());
        pageEvent.setFirst(page.isFirst());
        pageEvent.setContent(page.getContent());
        pageEvent.setTotalPages(page.getTotalPages());
        pageEvent.setNumberOfElements(page.getNumberOfElements());
        pageEvent.setTotalElements(page.getTotalElements());

        return pageEvent;
    }
}
