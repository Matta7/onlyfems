package fr.univrouen.onlyfems.config;

import fr.univrouen.onlyfems.constants.Roles;
import fr.univrouen.onlyfems.entities.User;
import fr.univrouen.onlyfems.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitUsersConfig implements CommandLineRunner {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitUsersConfig(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initUser("admin", Roles.ROLE_ADMIN);
        initUser("vip", Roles.ROLE_PRIVILEGED_USER);
        initUser("user", Roles.ROLE_USER);
    }

    /**
     * Init a user in database in order to test the API.
     *
     * @param userData Data that will be the user email, username and password.
     * @param role Role of the user.
     */
    public void initUser(String userData, Roles role) {
        User user = userRepository.findByEmail(userData + "@onlyfems.com");
        if (user == null) {
            user = new User();
            user.setEmail(userData + "@onlyfems.com");
            user.setUsername(userData);
            user.setPassword(passwordEncoder.encode(userData));
            user.setRoles(List.of(role));
            userRepository.save(user);
        }
    }
}
