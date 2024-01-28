package fr.univrouen.onlyfems.dto.authentication;

/**
 * DTO used for a login request.
 */
public class LoginDTO {
    private final String email;
    private final String password;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
