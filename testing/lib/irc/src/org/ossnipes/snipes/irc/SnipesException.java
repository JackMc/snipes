package org.ossnipes.snipes.irc;

public class SnipesException extends RuntimeException {

	private static final long serialVersionUID = 1564786435427928283L;

	public SnipesException() {
	}

	public SnipesException(String message) {
		super(message);
	}

	public SnipesException(Throwable cause) {
		super(cause);
	}

	public SnipesException(String message, Throwable cause) {
		super(message, cause);
	}

}
