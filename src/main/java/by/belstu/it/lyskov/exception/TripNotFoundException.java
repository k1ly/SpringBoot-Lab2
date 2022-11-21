package by.belstu.it.lyskov.exception;

public class TripNotFoundException extends Exception {

    public TripNotFoundException() {
        super();
    }

    public TripNotFoundException(String message) {
        super(message);
    }

    public TripNotFoundException(Throwable cause) {
        super(cause);
    }

    public TripNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}