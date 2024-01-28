package fr.univrouen.onlyfems.services;

import fr.univrouen.onlyfems.dto.image.ImageDTO;
import fr.univrouen.onlyfems.dto.image.ListImageDTO;
import fr.univrouen.onlyfems.dto.image.UploadImageDTO;
import fr.univrouen.onlyfems.entities.Image;
import fr.univrouen.onlyfems.exceptions.StorageException;
import fr.univrouen.onlyfems.exceptions.StorageFileNotFoundException;
import fr.univrouen.onlyfems.repositories.ImageRepository;
import fr.univrouen.onlyfems.utils.Pagination;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final IStorageService storageService;

    @Autowired
    public ImageService(ImageRepository imageRepository, FileSystemStorageService storageService) {
        this.imageRepository = imageRepository;
        this.storageService = storageService;
    }

    /**
     * Find an image in database using its ID.
     *
     * @param id ID of the image.
     * @return The image found.
     */
    public ImageDTO findById(int id) throws ObjectNotFoundException, StorageFileNotFoundException, IOException {
        Optional<Image> optionalImage = imageRepository.findById(id);
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            Resource imageResource = storageService.loadAsResource(getFileName(image.getId(), image.getName()));
            image.setBase64(Files.readAllBytes(imageResource.getFile().toPath()));
            return new ImageDTO(image);
        } else {
            throw new ObjectNotFoundException("Image not found in database", id);
        }
    }

    /**
     * Find all the images in database based on the publicity.
     * If publicity is true, return all the public images.
     * Otherwise, return all the images.
     * 
     * @param publicity The publicity of the image.
     * @param page The page number.
     * @param size The size of the page.
     *
     * @return The list of ImageDTO found.
     */
    public ListImageDTO findALl(Boolean publicity, int page, int size) throws StorageFileNotFoundException, IOException {
        Page<Image> p = getPage(publicity, page, size);

        for (Image image : p) {
            Resource imageResource = storageService.loadAsResource(getFileName(image.getId(), image.getName()));
            image.setBase64(imageResource.getContentAsByteArray());
        }

        return new ListImageDTO(p);
    }

    public Page<Image> getPage(Boolean publicity, int page, int size) {
        Page<Image> p;
        if (Boolean.TRUE.equals(publicity)) {
            p = imageRepository.findAllByPrivacy(true, Pagination.getPagination(page, size));
        } else {
            p = imageRepository.findAll(Pagination.getPagination(page, size));
        }
        return p;
    }

    /**
     * Create an image in database and in upload/ directory.
     *
     * @param saveRequest Request containing image information and file.
     * @return The image saved.
     */
    @Transactional
    public ImageDTO saveImage(UploadImageDTO saveRequest) throws StorageException, IOException {
        Image image = new Image(saveRequest.getName(), saveRequest.getDescription(), saveRequest.getPrivacy(), saveRequest.getContentType(), saveRequest.getBase64());
        if (isFileValid(image)) {

            Image newImage = imageRepository.save(image);
            storageService.store(image, getFileName(newImage.getId(), newImage.getName()));

            Resource imageResource = storageService.loadAsResource(getFileName(newImage.getId(), newImage.getName()));
            image.setBase64(Files.readAllBytes(imageResource.getFile().toPath()));
            return new ImageDTO(newImage);
        } else {
            throw new IllegalArgumentException("File given is not an image.");
        }
    }

    /**
     * Update the image in database and the file.
     *
     * @param updateRequest Request containing image information and file.
     * @param id ID of the image to update.
     * @return The image updated.
     */
    @Transactional
    public ImageDTO updateImage(UploadImageDTO updateRequest, int id) throws StorageException, IOException {
        Image image = imageRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Image not found in database", id));

        // If base 64 content has not changed.
        if (updateRequest.getBase64() == null || updateRequest.getBase64().equals("")) {

            // If file name has changed.
            if (updateRequest.getName() != null) {

                Resource oldImageResource = storageService.loadAsResource(getFileName(image.getId(), image.getName()));
                String oldFileName = image.getName();

                image.setName(updateRequest.getName());
                image.setDescription(updateRequest.getDescription());
                image.setPrivacy(updateRequest.getPrivacy());
                image.setContentType(updateRequest.getContentType());
                image.setBase64(oldImageResource.getContentAsByteArray());

                if (isFileValid(image)) {
                    storageService.store(image, getFileName(image.getId(), image.getName()));
                    Image updatedImage = imageRepository.save(image);
                    storageService.delete(getFileName(updatedImage.getId(), oldFileName));

                    return new ImageDTO(updatedImage);
                } else {
                    throw new IllegalArgumentException("File given is not an image.");
                }
            // File name has not changed.
            } else {

                image.setDescription(updateRequest.getDescription());
                image.setPrivacy(updateRequest.getPrivacy());
                image.setContentType(updateRequest.getContentType());
                Image updatedImage = imageRepository.save(image);
                Resource imageResource = storageService.loadAsResource(getFileName(image.getId(), image.getName()));
                updatedImage.setBase64(Files.readAllBytes(imageResource.getFile().toPath()));

                return new ImageDTO(updatedImage);
            }
        // Bytes has changed.
        } else {
            String oldFileName = image.getName();

            image.setName(updateRequest.getName());
            image.setDescription(updateRequest.getDescription());
            image.setPrivacy(updateRequest.getPrivacy());
            image.setContentType(updateRequest.getContentType());
            image.setBase64(updateRequest.getBase64());

            if (isFileValid(image)) {
                storageService.store(image, getFileName(image.getId(), image.getName()));
            } else {
                throw new IllegalArgumentException("File given is not an image.");
            }
            Image updatedImage = imageRepository.save(image);

            // If file name has changed.
            if (updateRequest.getName() != null && !oldFileName.equals(updateRequest.getName())) {
                storageService.delete(getFileName(updatedImage.getId(), oldFileName));
            }
            return new ImageDTO(updatedImage);
        }
    }

    /**
     * Delete the image using its ID.
     *
     * @param id ID of the image to delete.
     */
    @Transactional
    public void deleteImage(int id) throws StorageException {
        Optional<Image> imageOptional = imageRepository.findById(id);

        if (imageOptional.isPresent()) {
            Image image = imageOptional.get();
            imageRepository.delete(image);
            storageService.delete(getFileName(image.getId(), image.getName()));
        } else {
            throw new ObjectNotFoundException("Image not found in database", id);
        }
    }

    /**
     * Private method to check multiple verifications related to a MultipartFile object.
     *
     * @param file File to check.
     * @return true if the file is valid, false otherwise.
     */
    private boolean isFileValid(MultipartFile file) {
        // Check if file is not empty.
        if (file.isEmpty()) {
            return false;
        }

        // Check extension of the file.
        return file.getContentType().equals("image/png")
                || file.getContentType().equals("image/jpg")
                || file.getContentType().equals("image/jpeg")
                || file.getContentType().equals("image/svg")
                || file.getContentType().equals("image/gif");
    }

    /**
     * Private method to get the real file name.
     *
     * @param id ID of the image.
     * @param name Name of the file uploaded.
     * @return The name of the file.
     */
    private String getFileName(int id, String name) {
        String fileName = name;
        int lastDotIndex = fileName.lastIndexOf('.');
        fileName = fileName.substring(0, lastDotIndex ) + "_" + id + fileName.substring(lastDotIndex);
        return fileName;
    }
}