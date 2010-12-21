package org.ossnipes.snipes.exceptions;

/**
 * @author jack
 */
public class NoSnipesInstanceException extends Exception {

    /**
     * Creates a new instance of <code>NoSnipesInstanceException</code> without detail message.
     */
    public NoSnipesInstanceException() {
    }


    /**
     * Constructs an instance of <code>NoSnipesInstanceException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSnipesInstanceException(String msg) {
        super(msg);
    }
}
