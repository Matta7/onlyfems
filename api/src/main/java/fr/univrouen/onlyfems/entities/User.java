package fr.univrouen.onlyfems.entities;

import fr.univrouen.onlyfems.constants.Roles;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String email;

    private String username;

    private String password;

    @ElementCollection
    private List<String> roles;

    public User(String email, String username, String password, List<Roles> roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles.stream().map(Enum::name).toList();
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.equals("")) {
            this.email = email;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null && !username.equals("")) {
            this.username = username;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null && !password.equals("")) {
            this.password = password;
        }
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        if (roles != null && !roles.isEmpty()) {
            this.roles = roles.stream().map(Enum::name).toList();;
        }
    }

    public void addRole(Roles role) {
        roles.add(role.name());
    }

    public void removeRole(Roles role) {
        roles.remove(role.name());
    }

    public void clearRole() {
        roles.clear();
    }

    @Override
    public String toString() {
        return email;
    }
}
