package fr.univrouen.onlyfems.controllers;

import fr.univrouen.onlyfems.constants.APIEndpoints;
import fr.univrouen.onlyfems.dto.authentication.LoginDTO;
import fr.univrouen.onlyfems.dto.error.ErrorDTO;
import fr.univrouen.onlyfems.dto.user.UserDTO;
import fr.univrouen.onlyfems.services.AuthenticationService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Login route.
     *
     * @param loginRequest Login parameters.
     */
    @PostMapping(
            value = APIEndpoints.LOGIN_URL,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    @CrossOrigin(allowCredentials = "true", exposedHeaders = {"Set-Cookie"})
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginRequest, HttpServletRequest req) {
        try {
            authenticationService.login(loginRequest, req);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    /**
     * Logout route.
     */
    @PostMapping(
            value = APIEndpoints.LOGOUT_URL
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    public ResponseEntity<Object> logout() {
        try {
            SecurityContextHolder.clearContext();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    /**
     * Route that returns the current user authenticated.
     */
    @GetMapping(
            value = APIEndpoints.GET_AUTHENTICATED_USER,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    @CrossOrigin(allowCredentials = "true", exposedHeaders = {"Access-Control-Allow-Credentials"})
    public ResponseEntity<Object> getUserAuthenticated() {
        try {
            return ResponseEntity.ok(authenticationService.getAuthenticatedUser());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }
}
