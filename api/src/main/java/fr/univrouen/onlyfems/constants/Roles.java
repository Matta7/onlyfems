package fr.univrouen.onlyfems.constants;

/**
 * Enum that stock the list of roles.
 */
public enum Roles {

    /**
     * Role of a user connected with administrator permissions.
     */
    ROLE_ADMIN,

    /**
     * Role of a privileged user with permissions.
     */
    ROLE_PRIVILEGED_USER,

    /**
     * Role of a lambda user connected.
     */
    ROLE_USER,

    /**
     * role of a user that is not authenticated.
     */
    ROLE_ANONYMOUS
}
