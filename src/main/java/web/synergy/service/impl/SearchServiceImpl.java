package web.synergy.service.impl;

import web.synergy.domain.model.Article;
import web.synergy.domain.model.Event;
import web.synergy.domain.model.Searchable;
import web.synergy.dto.SearchResponse;
import web.synergy.service.SearchService;
import com.redis.om.spring.search.stream.EntityStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static web.synergy.dto.SearchResponse.ContentTypeEnum.ARTICLE;
import static web.synergy.dto.SearchResponse.ContentTypeEnum.EVENT;

/**
 * Service that implements {@link SearchService} contract.
 *
 * @author Evhen Malysh
 */
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final EntityStream entityStream;

    /**
     * Search posts by any string.
     *
     * @param query Client query for searching content.
     * @return List of founded posts.
     */
    @Override
    public List<SearchResponse> search(String query) {
        final CharSequence sequence = query.toLowerCase();

        return getSearchResults(sequence, Event.class, Article.class);
    }

    @SafeVarargs
    private List<SearchResponse> getSearchResults(
        final CharSequence sequence,
        Class<? extends Searchable>... searchables
    ) {
        var result = new ArrayList<SearchResponse>();

        for (var searchable : searchables) {
            try (var searchableStream = entityStream.of(searchable)) {
                var searchResults = searchableStream.collect(Collectors.toList());

                var eventSearchResponses = filterBySequence(searchResults, sequence);
                result.addAll(eventSearchResponses);
            }
        }
        return result;
    }

    private List<SearchResponse> filterBySequence(
            List<? extends Searchable> posts,
            final CharSequence sequence
    ) {
        return posts.parallelStream()
                .filter(searchPredicate(sequence))
                .map(this::toSearchResponse)
                .collect(Collectors.toList());
    }

    private <T extends Searchable> Predicate<T> searchPredicate(CharSequence sequence) {
        return post -> matchSequence(post.getTitle(), sequence)
                || matchSequence(post.getContent(), sequence);
    }

    private boolean matchSequence(String string, CharSequence sequence) {
        return string.toLowerCase().contains(sequence)
                || string.toLowerCase().matches(sequence.toString());
    }

    private SearchResponse toSearchResponse(Searchable post) {
        var type = post instanceof Event ? EVENT : ARTICLE;
        SearchResponse response = new SearchResponse();
        response.id(post.getSlug());
        response.title(post.getTitle());
        response.description(post.getContent());
        response.contentType(type);

        return response;
    }
}
