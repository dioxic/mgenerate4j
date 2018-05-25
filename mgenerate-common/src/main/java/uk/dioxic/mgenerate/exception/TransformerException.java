package uk.dioxic.mgenerate.exception;

public class TransformerException extends RuntimeException {

    public TransformerException(Class from, Class to) {
        this("Cannot transform " + from.getSimpleName() + " to " + to.getSimpleName());
    }

    public TransformerException(String message) {
        super(message);
    }

    public TransformerException(Exception e) {
        super(e);
    }

    public TransformerException(String message, Exception e) {
        super(message, e);
    }

    public TransformerException() {
        super();
    }
}
