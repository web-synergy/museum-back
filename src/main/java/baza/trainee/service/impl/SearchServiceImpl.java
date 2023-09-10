package baza.trainee.service.impl;

import baza.trainee.domain.dto.SearchDto;
import baza.trainee.service.SearchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Override
    public List<SearchDto> search(String query) {
        return null;
    }
}
