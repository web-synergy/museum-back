package baza.trainee.domain.model;


import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.Set;

/**
 * The domain model of the static content of a web page.
 *
 * @author Olha Ryzhkova
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document
public class Article {

    /**
     * Unique identifier.
     */
    @Id
    @Indexed
    private String id;

    /**
     * Article title.
     */
    @NotBlank
    @Searchable
    private String title;

    /**
     * Brief description of the article.
     */
    @NotBlank
    private String description;

    /**
     * Article text content.
     */
    @NotBlank
    @Searchable
    private String content;

    /**
     * Collection of links to images that are related to the article.
     */
    private Set<String> images;

    /**
     * The date the article was published.
     */
    @CreatedDate
    private LocalDate created;

    /**
     * The date the article was updated.
     */
    @LastModifiedDate
    private LocalDate updated;
}
