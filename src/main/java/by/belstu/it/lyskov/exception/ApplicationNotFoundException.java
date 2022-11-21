package by.belstu.it.lyskov.exception;

public class ApplicationNotFoundException extends Exception {

    public ApplicationNotFoundException() {
        super();
    }

    public ApplicationNotFoundException(String message) {
        super(message);
    }

    public ApplicationNotFoundException(Throwable cause) {
        super(cause);
    }

    public ApplicationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}