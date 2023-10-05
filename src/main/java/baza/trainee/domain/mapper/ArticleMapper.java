package baza.trainee.domain.mapper;

import baza.trainee.domain.model.Article;
import baza.trainee.dto.ArticleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleResponse toResponse(Article article);

}
