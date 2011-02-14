package org.ossnipes.snipes.bot;

/** An abstract superclass that represents that a problem has occurred with the
 * operation of the Snipes Module system or a Snipes Module; or in the loading
 * of one.
 * 
 * @author Jack McCracken (Auv5) */
public abstract class ModuleException extends Exception
{

	private static final long serialVersionUID = -5654904272122942983L;

	public ModuleException()
	{
	}

	public ModuleException(String message)
	{
		super(message);
	}

	public ModuleException(Throwable cause)
	{
		super(cause);
	}

	public ModuleException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
