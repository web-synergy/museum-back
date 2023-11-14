package web.synergy.domain.model;

/**
 * Interface of the text searchable object.
 *
 * @author Evhen Malysh
 */
public interface Searchable {

    /**
     * Getter for ID field.
     *
     * @return ID of the Post.
     */
    String getId();

    /**
     * Getter for ID field.
     *
     * @return ID of the Post.
     */
    String getSlug();

    /**
     * Title of the searchable object.
     * 
     * @return Title of the searchable object.
     */
    String getTitle();

    /**
     * Type of the searchable object.
     *
     * @return object type.
     */
    String getType();

    /**
     * Getter for text content.
     *
     * @return post title.
     */
    String getContent();
}
