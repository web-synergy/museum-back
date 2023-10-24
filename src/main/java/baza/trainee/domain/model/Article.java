package baza.trainee.domain.model;


import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

/**
 * The domain model of the static content of a web page.
 *
 * @author Olha Ryzhkova
 * @version 1.0
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Document
public class Article implements Searchable {

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
     * Article content.
     */
    @Indexed
    private String content;

    /**
     * Type of the searchable object.
     *
     * @return object type.
     */
    @Override
    public String getType() {
        return "static";
    }
}
