package baza.trainee.domain.model;

import com.redis.om.spring.annotations.Document;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * A class representing museum data.
 */
@Getter
@Setter
@NoArgsConstructor
@Document
public class MuseumData {

    /**
     * The unique identifier for the museum data.
     */
    @Id
    private String id;

    /**
     * The phone number associated with the museum.
     */
    private String phoneNumber;

    /**
     * The email address associated with the museum.
     */
    private String email;

    /**
     * The subway route information for reaching the museum.
     */
    private String subwayRoute;

    /**
     * The bus route information for reaching the museum.
     */
    private String busRoute;

    /**
     * The funicular route information for reaching the museum.
     */
    private String funicularRoute;
}
