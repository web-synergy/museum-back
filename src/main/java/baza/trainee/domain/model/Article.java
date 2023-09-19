package baza.trainee.domain.model;


import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

import static baza.trainee.constants.EventConstant.MAX_DESCRIPTION_SIZE;
import static baza.trainee.constants.EventConstant.MAX_TITLE_SIZE;
import static baza.trainee.constants.EventConstant.MIN_DESCRIPTION_SIZE;
import static baza.trainee.constants.EventConstant.MIN_TITLE_SIZE;

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
    @NotBlank
    @Size(min = MIN_TITLE_SIZE, max = MAX_TITLE_SIZE)
    private String title;

    /**
     * Brief description of the article.
     */
    @Indexed
    @NotBlank
    @Size(min = MIN_DESCRIPTION_SIZE, max = MAX_DESCRIPTION_SIZE)
    private String description;

    /**
     * Article content.
     */
    @NotEmpty
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
    public void addContentBlock(@Valid final ContentBlock block) {
        this.content.add(block);
    }
}
