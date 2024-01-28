package fr.univrouen.onlyfems.services;

import fr.univrouen.onlyfems.entities.User;
import fr.univrouen.onlyfems.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom UserDetailsService used in SecurityConfig to load users.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Method to load a user in database using the username.
     *
     * @param username Username of the user.
     * @return UserDetails object.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userRepo = userRepository.findByEmail(username);
        return new org.springframework.security.core.userdetails.User(
                userRepo.getEmail(),
                userRepo.getPassword(),
                getAuthorities(userRepo.getRoles())
        );
    }

    /**
     * Private method to get a list of GrantedAuthority object with a list of String object.
     *
     * @param roles The list of roles to convert.
     * @return The roles converted in GrantedAuthority.
     */
    private List<GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
