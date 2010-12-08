package org.ossnipes.snipes.exceptions;

public class SnipesNotConfiguredException extends Exception {

    private static final long serialVersionUID = 395373299526248510L;

    public SnipesNotConfiguredException() {
    }

    public SnipesNotConfiguredException(String message) {
        super(message);
    }

    public SnipesNotConfiguredException(Throwable cause) {
        super(cause);
    }

    public SnipesNotConfiguredException(String message, Throwable cause) {
        super(message, cause);
    }
}
