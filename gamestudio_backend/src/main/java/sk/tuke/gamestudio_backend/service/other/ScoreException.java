package sk.tuke.gamestudio_backend.service.other;

public class ScoreException extends Exception {
    public ScoreException(String message) {
        super(message);
    }

    public ScoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
