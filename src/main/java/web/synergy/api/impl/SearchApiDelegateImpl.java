package web.synergy.api.impl;

import web.synergy.api.SearchApiDelegate;
import web.synergy.dto.SearchResponse;
import web.synergy.service.SearchService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchApiDelegateImpl implements SearchApiDelegate {

    private final SearchService searchService;

    @Override
    public ResponseEntity<List<SearchResponse>> search(String query) {
        return new ResponseEntity<>(searchService.search(query), HttpStatus.OK);
    }
}
