package handler;

public class BadInputException extends RuntimeException {
    public BadInputException(String message) {
        super(message);
    }
    public BadInputException(String message, Throwable ex) {
        super(message, ex);
    }
}
