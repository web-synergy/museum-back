package baza.trainee.domain.model;

import java.util.Collection;

/**
 * Interface of the web post.
 *
 * @author Evhen Malysh
 */
public interface Post {

    /**
     * Getter for ID field.
     *
     * @return ID of the Post.
     */
    String getId();

    /**
     * Getter for Title field.
     *
     * @return post title.
     */
    String getTitle();

    /**
     * Getter for Description field.
     *
     * @return post description.
     */
    String getDescription();

    /**
     * Getter for content field.
     *
     * @return collection of {@link ContentBlock}.
     */
    Collection<ContentBlock> getContent();
}
