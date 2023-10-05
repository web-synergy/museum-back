package baza.trainee.domain.model;


import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;

import baza.trainee.dto.ContentBlock;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.HashSet;
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
@Builder(toBuilder = true)
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
    @Indexed
    private String title;

    /**
     * Brief description of the article.
     */
    @Indexed
    private String description;

    /**
     * Article content.
     */
    @Builder.Default
    private Set<ContentBlock> content = new HashSet<>();

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

    /**
     * Add block of content to the article.
     * @param block block of content
     */
    public void addContentBlock(final ContentBlock block) {
        this.content.add(block);
    }
}
