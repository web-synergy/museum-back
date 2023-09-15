package baza.trainee.domain.model;

import baza.trainee.domain.enums.BlockType;
import com.redis.om.spring.annotations.Document;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@Document
public class ContentBlock {

    @Id
    private String id;

    @NotNull
    @Positive
    private Integer order;

    @NotNull
    @Positive
    private Integer columns;

    @NotNull
    private BlockType blockType;

    private String textContent;

    private String pictureLink;

}
