package web.synergy.domain.mapper;


import web.synergy.domain.model.Event;
import web.synergy.dto.EventDraft;
import web.synergy.dto.EventPublication;
import web.synergy.dto.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for {@link Event} entity.
 *
 */
@Mapper(componentModel = "spring")
public interface EventMapper {

    /**
     * Provides mapping to {@link Event} from {@link EventPublication}.
     *
     * @param publication {@link EventPublication} DTO.
     * @return mapped Event.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Event toEvent(EventPublication publication);

    @Mapping(target = "created", dateFormat = "yyyy-MM-dd")
    EventResponse toResponse(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Event toEvent(EventDraft eventDraft);

}
