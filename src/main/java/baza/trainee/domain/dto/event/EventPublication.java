package baza.trainee.domain.dto.event;

import baza.trainee.domain.model.ContentBlock;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public record EventPublication(

        @NotBlank
        @NotNull
        String title,

        @NotBlank
        @NotNull
        String description,

        @NotNull
        @NotBlank
        String type,

        Set<String> tags,

        Set<ContentBlock> content,

        String bannerTempURI,

        @FutureOrPresent
        LocalDate begin,

        @FutureOrPresent
        LocalDate end
) {
    public EventPublication {
        if (begin.isAfter(end)) {
            throw new IllegalArgumentException("The 'end' date must be after 'before' the date!");
        }
    }
}
