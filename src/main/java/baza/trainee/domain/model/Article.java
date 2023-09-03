package baza.trainee.domain.model;


import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document
public class Article {

    @Id
    @Indexed
    private String id;

    @NonNull
    @Searchable
    private String title;

    @NonNull
    private String description;

    @NonNull
    @Searchable
    private String content;

    private Set<String> images;

    private LocalDate created;

    private LocalDate updated;
}
