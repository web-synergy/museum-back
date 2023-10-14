package baza.trainee.domain.model;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document
public class Event implements Searchable {

    @Id
    @Indexed
    private String id;

    @Indexed
    private String title;

    @Indexed
    private String summary;

    @Indexed
    private String description;

    private String type;

    private String banner;

    private LocalDate begin;

    private LocalDate end;

    @CreatedDate
    private LocalDate created;

    @LastModifiedDate
    private LocalDate updated;

    public Event(
            String id,
            String title,
            String summary,
            String description, String type,
            String bannerId,
            LocalDate begin,
            LocalDate end
    ) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.type = type;
        this.banner = bannerId;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String getContent() {
        return this.getSummary() + " " + this.getDescription();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, summary, type, description, banner, begin, end);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Event other)) {
            return false;
        }
        return Objects.equals(id, other.id)
                && Objects.equals(title, other.title)
                && Objects.equals(summary, other.summary)
                && Objects.equals(type, other.type)
                && Objects.equals(description, other.description)
                && Objects.equals(banner, other.banner)
                && Objects.equals(end, other.end);
    }
}
