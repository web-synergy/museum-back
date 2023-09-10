package baza.trainee.constants;

/**
 * This class defines constants related to
 * {@link baza.trainee.controller.SearchController}.
 * It includes constants for minimum and maximum query sizes,
 * as well as error messages.
 */
public final class SearchConstant {

    /**
     * The minimum allowed size for a search query.
     */
    public static final byte MIN_SIZE_QUERY = 3;

    /**
     * The maximum allowed size for a search query.
     */
    public static final byte MAX_SIZE_QUERY = 120;

    /**
     * Error message indicating that a search query should not be blank.
     */
    public static final String BLANK_QUERY_ERROR_MESSAGE = "Query should not"
            + " be blank";

    /**
     * Error message indicating that the size of a query
     * should be between {@link #MIN_SIZE_QUERY}
     * and {@link #MAX_SIZE_QUERY} characters.
     */
    public static final String SIZE_QUERY_ERROR_MESSAGE = "Size of query"
            + " should be between " + MIN_SIZE_QUERY
            + " and " + MAX_SIZE_QUERY + " characters";

    private SearchConstant() { }
}
