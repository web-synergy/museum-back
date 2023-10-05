package baza.trainee.service;


import baza.trainee.dto.SearchResponse;

import java.util.List;

public interface SearchService {

    /**
     * Search events and articles.
     *
     * @param query Client query for searching content.
     * @return {@link SearchResponse} list.
     */
    List<SearchResponse> search(String query);
}
