package baza.trainee.service.impl;

import baza.trainee.domain.dto.SearchResponse;
import baza.trainee.domain.enums.ContentType;
import baza.trainee.domain.model.Event;
import baza.trainee.domain.model.Post;
import baza.trainee.service.SearchService;
import com.redis.om.spring.search.stream.EntityStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

        var events = entityStream.of(Event.class)
                .collect(Collectors.toList());

        var posts = new ArrayList<Post>();
        posts.addAll(events);

        return filterBySequence(posts, sequence);
    }

    private <T extends Post> List<SearchResponse> filterBySequence(
            List<T> posts,
            final CharSequence sequence
    ) {
        return posts.parallelStream()
                .filter(searchPredicate(sequence))
                .map(this::toSearchResponse)
                .collect(Collectors.toList());
    }

    private <T extends Post> Predicate<T> searchPredicate(CharSequence sequence) {
        return post -> matchSequence(post.getTitle(), sequence)
                || matchSequence(post.getDescription(), sequence)
                || post.getContent().stream().anyMatch(c -> matchSequence(c.getTextContent(), sequence));
    }

    private boolean matchSequence(String string, CharSequence sequence) {
        return string.toLowerCase().contains(sequence);
    }

    private SearchResponse toSearchResponse(Post post) {
        var type = post instanceof Event ? ContentType.EVENT : ContentType.ARTICLE;
        return new SearchResponse(post.getId(), post.getTitle(), post.getDescription(), type);
    }
}
