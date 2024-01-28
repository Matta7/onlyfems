package fr.univrouen.onlyfems.constants;

/**
 * Enum that contains API endpoints constants and associated permissions.
 */
public class APIEndpoints {

    /**
     * Login URL
     */
    public static final String LOGIN_URL = "/authentication/login";

    /**
     * Logout URL
     */
    public static final String LOGOUT_URL = "/authentication/logout";

    /**
     * URL to get the current user logged.
     */
    public static final String GET_AUTHENTICATED_USER = "/authentication/user";

    /**
     * URL to create and list users.
     */
    public static final String USERS_URL = "/users";

    /**
     * URL to get, update or delete a user by its ID.
     */
    public static final String USERS_ID_URL = "/users/{id}";

    /**
     * URL to create and list images.
     */
    public static final String IMAGES_URL = "/images";

    /**
     * URL to get, update or delete an image by its ID.
     */
    public static final String IMAGES_ID_URL = IMAGES_URL + "/{id}";
}
