package baza.trainee.domain.mapper;


import baza.trainee.domain.model.Event;
import baza.trainee.dto.EventPublication;
import baza.trainee.dto.EventResponse;
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
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "id", ignore = true)
    Event toEvent(EventPublication publication);

    EventResponse toResponse(Event event);

}
