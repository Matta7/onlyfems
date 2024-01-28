package fr.univrouen.onlyfems.services;

import fr.univrouen.onlyfems.constants.Roles;
import fr.univrouen.onlyfems.dto.authentication.LoginDTO;
import fr.univrouen.onlyfems.dto.user.UserDTO;
import fr.univrouen.onlyfems.entities.User;
import fr.univrouen.onlyfems.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/**
 * Service containing all methods bound to authentication.
 */
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleHierarchy roleHierarchy;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 UserRepository userRepository, RoleHierarchy roleHierarchy) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleHierarchy = roleHierarchy;
    }

    /**
     * Login a user provided a login request.
     */
    public void login(LoginDTO loginDTO, HttpServletRequest req) {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
    }

    public void reLogin(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
        for (String role : user.getRoles()) {
            updatedAuthorities.add(new SimpleGrantedAuthority(role));
        }

        Authentication newAuth = new UsernamePasswordAuthenticationToken(user.getEmail(), auth.getCredentials(), updatedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    /**
     * Return the user currently authenticated.
     * Can return an anonymous user if there is no user authenticated.
     *
     * @return The user.
     */
    public UserDTO getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDTO userDTO;
        User user = userRepository.findByEmail(authentication.getName());
        if (isAuthenticated() && user != null) {
            userDTO = new UserDTO(
                    user.getId(),
                    authentication.getName(),
                    user.getUsername(),
                    authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
            );
        } else {
            userDTO = new UserDTO(
                    null,
                    null,
                    null,
                    authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
            );
        }

        return userDTO;
    }


    // Private methods

    /**
     * Return true if a user is authenticated, false otherwise.
     *
     * @return A boolean.
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return !(authentication instanceof AnonymousAuthenticationToken);
        } else {
            return false;
        }
    }

    /**
     * Check among the role hierarchy if user has access to a role.
     *
     * @param role Role to check.
     * @return True if user has access, false otherwise.
     */
    public boolean hasAccess(Roles role) {
        List<GrantedAuthority> authorities = new ArrayList<>(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return roleHierarchy.getReachableGrantedAuthorities(authorities).contains(new SimpleGrantedAuthority(role.name()));
    }
}
