package baza.trainee.service;

import baza.trainee.domain.dto.SearchDto;

import java.util.List;

public interface SearchService {

    /**
     * Search events and articles.
     *
     * @param query Client query for searching content.
     * @return {@link SearchDto} list.
     */
    List<SearchDto> search(String query);
}
