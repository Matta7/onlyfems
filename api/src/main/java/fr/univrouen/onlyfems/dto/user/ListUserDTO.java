package fr.univrouen.onlyfems.dto.user;

import fr.univrouen.onlyfems.entities.User;

import java.util.List;

public class ListUserDTO {
    private final List<UserDTO> users;

    public ListUserDTO(List<UserDTO> users) {
        this.users = users;
    }

    public List<UserDTO> getUsers() {
        return users;
    }
}
