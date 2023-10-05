package baza.trainee.domain.mapper;

import org.mapstruct.Mapper;

import baza.trainee.domain.model.MuseumData;
import baza.trainee.dto.MuseumInfo;

@Mapper(componentModel = "spring")
public interface MuseumDataMapper {

    MuseumData toMuseumData(MuseumInfo info);

}
