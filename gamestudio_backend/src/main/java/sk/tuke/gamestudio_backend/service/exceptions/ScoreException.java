package sk.tuke.gamestudio_backend.service.exceptions;

public class ScoreException extends Exception {
    public ScoreException(String message) {
        super(message);
    }

    public ScoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
