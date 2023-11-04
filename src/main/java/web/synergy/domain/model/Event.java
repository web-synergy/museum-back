package web.synergy.domain.model;

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
import java.time.LocalDateTime;
import java.util.Objects;

import static web.synergy.dto.EventPublication.TypeEnum.OTHER;

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
    private String slug;

    @Indexed
    private String title;

    @Indexed
    private String summary;

    @Indexed
    private String description;

    private String type;

    private String banner;

    @Indexed
    private String status;

    private LocalDate begin;

    private LocalDate end;

    @CreatedDate
    private LocalDate created;

    @LastModifiedDate
    private LocalDate updated;

    public void updateSlug() {
        var targetType = this.type == null ? OTHER.getValue() : this.type;
        this.slug = targetType.toLowerCase() + "-" + LocalDateTime.now().getNano();
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
