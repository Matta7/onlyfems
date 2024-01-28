package fr.univrouen.onlyfems.dto.user;

import fr.univrouen.onlyfems.constants.Roles;

import java.util.List;

/**
 * DTO used to create or update users.
 */
public class SaveUserDTO {
    private final String email;
    private final String username;
    private final String password;
    private final String confirmPassword;
    private final List<Roles> roles;

    public SaveUserDTO(String email, String username, String password, String confirmPassword, List<Roles> roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public List<Roles> getRoles() {
        return roles;
    }
}
