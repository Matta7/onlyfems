package fr.univrouen.onlyfems.services;

import fr.univrouen.onlyfems.constants.Roles;
import fr.univrouen.onlyfems.dto.user.ListUserDTO;
import fr.univrouen.onlyfems.dto.user.SaveUserDTO;
import fr.univrouen.onlyfems.dto.user.UserDTO;
import fr.univrouen.onlyfems.entities.User;
import fr.univrouen.onlyfems.exceptions.UnauthorizedException;
import fr.univrouen.onlyfems.repositories.UserRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service to interact with users in database.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    /**
     * Return a user using an ID.
     *
     * @param id ID of a user.
     * @return The user if found.
     */
    public UserDTO getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, User.class.getName()));

        return new UserDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return new UserDTO(user);
    }

    /**
     * Return the list of users in database.
     *
     * @return The list of users.
     */
    public ListUserDTO listUsers() {
        List<UserDTO> users = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            users.add(new UserDTO(user));
        }

        return new ListUserDTO(users);
    }

    /**
     * Create a user in database.
     *
     * @param createRequest The request containing user data to create.
     * @return The user created.
     */
    public UserDTO createUser(SaveUserDTO createRequest) {
        User user = new User(createRequest.getEmail(), createRequest.getUsername(), passwordEncoder.encode(createRequest.getPassword()), createRequest.getRoles());
        checkUserData(user);
        return new UserDTO(userRepository.save(user));
    }

    /**
     * Update a user in database.
     *
     * @param id ID of the user to update.
     * @param updateRequest Update request containing user data.
     * @return The user updated.
     */
    public UserDTO updateUser(int id, SaveUserDTO updateRequest) throws UnauthorizedException {
        User user = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, User.class.getName()));

        boolean reAuth = false;
        if (user.getEmail().equals(authenticationService.getAuthenticatedUser().getEmail())) {
            reAuth = true;
        }

        // Check if email has changed and check if email exists.
        if ((updateRequest.getEmail() != null) && !user.getEmail().equals(updateRequest.getEmail()) && (userRepository.findByEmail(user.getEmail()) == null)) {
            throw new IllegalArgumentException("L'email \"" + user.getEmail() + "\" existe déjà.");
        }

        // An admin update a user.
        if (authenticationService.hasAccess(Roles.ROLE_ADMIN)) {
            user.setEmail(updateRequest.getEmail());
            user.setUsername(updateRequest.getUsername());

            // Check if password has changed.
            if (updateRequest.getPassword() != null && !updateRequest.getPassword().equals("")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            // If roles have changed.
            if (updateRequest.getRoles() != null && !updateRequest.getRoles().isEmpty()) {
                user.clearRole();

                for (Roles roles : updateRequest.getRoles()) {
                    user.addRole(roles);
                }
            }

            checkUserData(user);
            User updatedUser = userRepository.save(user);

            // Re authenticate the current user.
            if (reAuth) {
                authenticationService.reLogin(updatedUser);
            }

            return new UserDTO(updatedUser);

        // User is not admin.
        } else {
            // If user attempt to change the roles.
            if (updateRequest.getRoles() != null && !updateRequest.getRoles().isEmpty()) {
                throw new UnauthorizedException("Aucune autorisation pour modifier les rôles d'un utilisateur.");
            }

            // Check if the password confirmation is the same as user password.
            if (updateRequest.getConfirmPassword() == null) {
                throw new IllegalArgumentException("Le mot de passe de confirmation est vide" +
                        ".");
            }
            if (passwordEncoder.matches(updateRequest.getConfirmPassword(), user.getPassword())) {

                user.setEmail(updateRequest.getEmail());
                user.setUsername(updateRequest.getUsername());

                // Check il password has changed.
                if (updateRequest.getPassword() != null && !updateRequest.getPassword().equals("")) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }

                checkUserData(user);
                User updatedUser = userRepository.save(user);

                // Re authenticate the current user.
                if (reAuth) {
                    authenticationService.reLogin(updatedUser);
                }

                return new UserDTO(updatedUser);
            }
            throw new IllegalArgumentException("Le mot de passe de vérification n'est pas correcte.");
        }
    }

    /**
     * Delete a user in database using an ID.
     *
     * @param id ID of a user.
     */
    public void deleteUserWithId(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, User.class.getName()));
        userRepository.delete(user);
    }


    // Private methods.

    /**
     * Check if user data are consistent.
     *
     * @param user User to check.
     */
    private void checkUserData(User user) {
        if (!verifyEmail(user.getEmail())) {
            throw new IllegalArgumentException("L'email n'est pas bien formé.");
        }

        if (!verifyPassword(user.getPassword())) {
            throw new IllegalArgumentException("Le mot de passe n'est pas bien formé.");
        }
    }

    /**
     * Private method to check if a string in parameter is an email.
     *
     * @param email Email to check.
     * @return true if parameter is an email, false otherwise.
     */
    private boolean verifyEmail(String email) {
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return email.matches(regex);
    }

    /**
     * Private method to check if a password contains at least one letter, one number,
     * and has at least 8 characters.
     *
     * @param password The password to check.
     * @return true if password is correct, false otherwise.
     */
    private boolean verifyPassword(String password) {
        return password.matches(".*[A-Za-z].*")
                && password.matches(".*[0-9].*")
                && password.length() >= 8;
    }
}
