package baza.trainee.domain.mapper;

import baza.trainee.domain.dto.ArticleDto;
import baza.trainee.domain.model.Article;
import org.mapstruct.Mapper;

/**
 * Map article models within domain and web layers.
 *
 * @author Olha Ryzhkova
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface ArticleMapper {

    /**
     * Maps article web model to the domain model.
     * @param articleDto article web model.
     * @return {@link Article} domain model.
     */
    Article toArticle(ArticleDto articleDto);

    /**
     * Maps article domain model to the web model.
     * @param article domain model.
     * @return {@link ArticleDto} article web model.
     */
    ArticleDto toDto(Article article);
}
