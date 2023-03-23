package sk.tuke.gamestudio_backend.service.exceptions;

public class RatingException extends RuntimeException {
    public RatingException(String message) {
        super(message);
    }

    public RatingException(String message, Throwable cause) {
        super(message, cause);
    }
}
