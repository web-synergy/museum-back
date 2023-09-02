package baza.trainee.controller;

import baza.trainee.domain.dto.SearchDto;
import baza.trainee.service.SearchService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/{query}")
    List<SearchDto> search(@NotBlank(message = "Query should not be blank")
                           @Size(min = 3, max = 120, message = "Size of query should be between 3 and 120 characters")
                           @PathVariable("query") String query) {
        return searchService.search(query);
    }
}
