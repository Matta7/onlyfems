package fr.univrouen.onlyfems.exceptions;

public class UnauthorizedException extends StorageException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
