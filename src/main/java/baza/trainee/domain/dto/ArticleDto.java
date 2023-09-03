package baza.trainee.domain.dto;

import java.time.LocalDate;
import java.util.Set;

public record ArticleDto(
        String id,
        String title,
        String description,
        String content,
        Set<String> images,
        LocalDate created,
        LocalDate updated
) {
}
