package fr.univrouen.onlyfems.controllers;

import fr.univrouen.onlyfems.constants.APIEndpoints;
import fr.univrouen.onlyfems.constants.Roles;
import fr.univrouen.onlyfems.dto.error.ErrorDTO;
import fr.univrouen.onlyfems.dto.user.ListUserDTO;
import fr.univrouen.onlyfems.dto.user.SaveUserDTO;
import fr.univrouen.onlyfems.dto.user.UserDTO;
import fr.univrouen.onlyfems.exceptions.UnauthorizedException;
import fr.univrouen.onlyfems.services.AuthenticationService;
import fr.univrouen.onlyfems.services.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    /**
     * Route to get a user in database.
     *
     * @param id ID of the user to get.
     * @return User DTO.
     */
    @GetMapping(
            value = APIEndpoints.USERS_ID_URL,
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
            ),
            @ApiResponse(
                    responseCode = "401"
            ),
            @ApiResponse(
                    responseCode = "403"
            ),
            @ApiResponse(
                    responseCode = "404"
            )
    })
    public ResponseEntity<Object> getUser(@PathVariable int id) {
        if (!authenticationService.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        if (!authenticationService.hasAccess(Roles.ROLE_ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    /**
     * Route to get all user in database.
     *
     * @return List of user DTO.
     */
    @GetMapping(
            value = APIEndpoints.USERS_URL,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ListUserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401"
            ),
            @ApiResponse(
                    responseCode = "403"
            ),
    })
    public ResponseEntity<Object> listUser() {
        if (!authenticationService.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        if (!authenticationService.hasAccess(Roles.ROLE_ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        try {
            return ResponseEntity.ok(userService.listUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    /**
     * Route to create a user in database.
     *
     * @param request Request to save the user int database.
     * @return The DTO of user created.
     */
    @PostMapping(
            value = APIEndpoints.USERS_URL,
            consumes = MediaType.APPLICATION_JSON_VALUE,
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
    public ResponseEntity<Object> createUser(@RequestBody SaveUserDTO request) {
        try {
            return ResponseEntity.ok(userService.createUser(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    /**
     * Route to update a user in database.
     *
     * @param id ID of the user to update.
     * @param request Request to update user.
     * @return The DTO of user updated.
     */
    @PatchMapping(
            value = APIEndpoints.USERS_ID_URL,
            consumes = MediaType.APPLICATION_JSON_VALUE,
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
            ),
            @ApiResponse(
                    responseCode = "401"
            ),
            @ApiResponse(
                    responseCode = "403"
            ),
            @ApiResponse(
                    responseCode = "404"
            )
    })
    public ResponseEntity<Object> updateUser(@PathVariable int id, @RequestBody SaveUserDTO request) {
        if (!authenticationService.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        if (!authenticationService.hasAccess(Roles.ROLE_USER)) {
            return ResponseEntity.status(403).build();
        }
        try {
            return ResponseEntity.ok(userService.updateUser(id, request));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(403).body(new ErrorDTO(e.getMessage()));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    /**
     * Route to delete a user in database.
     *
     * @param id ID of the user to delete.
     * @return 204.
     */
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401"
            ),
            @ApiResponse(
                    responseCode = "403"
            ),
            @ApiResponse(
                    responseCode = "404"
            )
    })
    @DeleteMapping(
            value = APIEndpoints.USERS_ID_URL,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        if (!authenticationService.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        if (!authenticationService.hasAccess(Roles.ROLE_ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        try {
            userService.deleteUserWithId(id);
            return ResponseEntity.noContent().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }
}
