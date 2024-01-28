package fr.univrouen.onlyfems.services;

import fr.univrouen.onlyfems.exceptions.StorageException;
import fr.univrouen.onlyfems.exceptions.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Interface to manage a storage.
 */
public interface IStorageService {

    /**
     * Initialize the upload directory.
     *
     * @throws StorageException
     */
    void init() throws StorageException;

    /**
     * Store a file given in parameter.
     *
     * @param file to store.
     * @param filename Name of the file to store.
     * @throws StorageException
     */
    void store(MultipartFile file, String filename) throws StorageException;

    /**
     * Load all files in storage.
     *
     * @return All files loaded.
     * @throws StorageException
     */
    Stream<Path> loadAll() throws StorageException;

    /**
     * Load a file path using its filename.
     *
     * @param filename Name of the file to load.
     * @return The path of the file loaded.
     */
    Path load(String filename);

    /**
     * Load a file using its filename.
     *
     * @param filename Name of the file to load.
     * @return An object Resource.
     * @throws StorageFileNotFoundException
     */
    Resource loadAsResource(String filename) throws StorageFileNotFoundException;

    /**
     * Delete all files.
     */
    void deleteAll();

    /**
     * Delete a file.
     *
     * @param filename Name of the file to delete.
     */
    void delete(String filename) throws StorageException;
}
