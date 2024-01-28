package fr.univrouen.onlyfems.controllers;

import fr.univrouen.onlyfems.constants.Roles;
import fr.univrouen.onlyfems.dto.error.ErrorDTO;
import fr.univrouen.onlyfems.dto.image.ImageDTO;
import fr.univrouen.onlyfems.dto.image.ListImageDTO;
import fr.univrouen.onlyfems.dto.image.UploadImageDTO;
import fr.univrouen.onlyfems.services.AuthenticationService;
import fr.univrouen.onlyfems.services.ImageService;
import fr.univrouen.onlyfems.constants.APIEndpoints;
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
public class ImageController {

    private final ImageService imageService;
    private final AuthenticationService authenticationService;

    @Autowired
    public ImageController(ImageService imageService, AuthenticationService authenticationService) {
        this.imageService = imageService;
        this.authenticationService = authenticationService;
    }

    /**
     * Find an image using its ID.
     *
     * @param id ID of the image.
     * @return The image found.
     */
    @GetMapping(
            value = APIEndpoints.IMAGES_ID_URL,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ImageDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404"
            )
    })
    public ResponseEntity<Object> getImage(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(imageService.findById(id));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    /**
     *  Retrieve all images.
     */
    @GetMapping(
            value = APIEndpoints.IMAGES_URL,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ListImageDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
            )
    })
    public ResponseEntity<Object> getAllImages(
        @RequestParam(defaultValue = "false", required = false) Boolean publicity,
        @RequestParam(defaultValue = "1", required = true) int page,
        @RequestParam(defaultValue = "10", required = false) int size
    ) {
        if (!authenticationService.hasAccess(Roles.ROLE_PRIVILEGED_USER)) {
            publicity = true;
        }
        try {
            return ResponseEntity.ok(imageService.findALl(publicity, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    /**
     * Upload an image on the API.
     *
     * @param request Upload request to save the image.
     * @return The image uploaded.
     */
    @PostMapping(
            value = APIEndpoints.IMAGES_URL,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ImageDTO.class))
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
    public ResponseEntity<Object> uploadImage(@RequestBody UploadImageDTO request) {
        if (!authenticationService.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        if (!authenticationService.hasAccess(Roles.ROLE_ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        try {
            return ResponseEntity.ok(imageService.saveImage(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    /**
     * Update an image.
     *
     * @param request Upload request to update the image.
     * @param id ID of the file to update.
     * @return The image DTO of the updated image.
     */
    @PatchMapping(
            value = APIEndpoints.IMAGES_ID_URL,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ImageDTO.class))
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
    public ResponseEntity<Object> updateImage(@RequestBody UploadImageDTO request, @PathVariable Integer id) {
        if (!authenticationService.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        if (!authenticationService.hasAccess(Roles.ROLE_ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        try {
            return ResponseEntity.ok(imageService.updateImage(request, id));
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    /**
     * Delete an image.
     *
     * @param id ID of the image to delete.
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
            value = APIEndpoints.IMAGES_ID_URL,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> deleteImage(@PathVariable Integer id) {
        if (!authenticationService.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        if (!authenticationService.hasAccess(Roles.ROLE_ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        try {
            imageService.deleteImage(id);
            return ResponseEntity.noContent().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }
}