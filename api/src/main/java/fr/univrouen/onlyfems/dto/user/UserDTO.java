package fr.univrouen.onlyfems.dto.user;

import fr.univrouen.onlyfems.entities.User;

import java.util.List;

public class UserDTO {
    private final Integer id;
    private final String email;
    private final String username;
    private final List<String> roles;

    public UserDTO(Integer id, String email, String username, List<String> roles) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }

    public UserDTO(User user) {
        id = user.getId();
        email = user.getEmail();
        username = user.getUsername();
        roles = user.getRoles();
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
