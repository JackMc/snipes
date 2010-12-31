package org.ossnipes.snipes.lib.irc;

/**
 * A exception thrown when a operation is attempted that would require
 * connection to a IRC server, and we are not connected.
 * 
 * @author jack
 * 
 */
public class NotConnectedException extends RuntimeException
{
	private static final long serialVersionUID = 7694269847054717555L;

	public NotConnectedException()
	{
		super();
	}

	public NotConnectedException(String message)
	{
		super(message);
	}

	public NotConnectedException(Throwable cause)
	{
		super(cause);
	}

	public NotConnectedException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
