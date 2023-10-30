package web.synergy.domain.mapper;

import org.mapstruct.Mapper;

import web.synergy.domain.model.MuseumData;
import web.synergy.dto.MuseumInfo;

@Mapper(componentModel = "spring")
public interface MuseumDataMapper {

    MuseumData toMuseumData(MuseumInfo info);

}
