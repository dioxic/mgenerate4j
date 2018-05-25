package uk.dioxic.mgenerate.exception;

public class WrapException extends RuntimeException {

    public WrapException(Class from, Class to) {
        this("Cannot wrap " + from.getSimpleName() + " to " + to.getSimpleName());
    }

    public WrapException(String message) {
        super(message);
    }

    public WrapException(Exception e) {
        super(e);
    }

    public WrapException(String message, Exception e) {
        super(message, e);
    }

    public WrapException() {
        super();
    }
}
