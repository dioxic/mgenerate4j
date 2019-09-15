package uk.dioxic.mgenerate.common.exception;

import uk.dioxic.mgenerate.common.Resolvable;

public class ResolveException extends RuntimeException {

    public ResolveException(String message) {
        super(message);
    }

    public ResolveException(Resolvable resolvable) {
        super("cannot resolve value for " + resolvable.getClass().getSimpleName());
    }

    public ResolveException(Resolvable resolvable, Exception e) {
        super("cannot resolve value for " + resolvable.getClass().getSimpleName(), e);
    }

    public ResolveException() {
        super();
    }
}
