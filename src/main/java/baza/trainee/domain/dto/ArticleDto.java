package baza.trainee.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Set;

import static baza.trainee.constants.ArticleConstant.CANNOT_BE_EMPTY;
import static baza.trainee.constants.ArticleConstant.CONTENT;
import static baza.trainee.constants.ArticleConstant.DESCRIPTION;
import static baza.trainee.constants.ArticleConstant.TITLE;

public record ArticleDto(
        String id,
        @NotBlank(message = TITLE + CANNOT_BE_EMPTY)
        String title,
        @NotBlank(message = DESCRIPTION + CANNOT_BE_EMPTY)
        String description,
        @NotBlank(message = CONTENT + CANNOT_BE_EMPTY)
        String content,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Set<String> images,
        LocalDate created,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDate updated
) {
}
