package uk.dioxic.mgenerate.common.exception;

public class DocumentNotMappedException extends RuntimeException {

    public DocumentNotMappedException(String message) {
        super(message);
    }

    public DocumentNotMappedException() {
        super();
    }
}
