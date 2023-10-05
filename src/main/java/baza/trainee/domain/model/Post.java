package baza.trainee.domain.model;

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
    String getSummary();

    /**
     * Getter for content field.
     *
     * @return collection of {@link ContentBlock}.
     */
    String getDescription();
}
