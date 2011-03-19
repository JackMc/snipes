package org.ossnipes.snipes.bot;

/** Signals that there was a error when loading a Snipes module.
 * 
 * @author Jack McCracken (Auv5)
 * @since Snipes 0.01 */
public class ModuleLoadException extends ModuleException
{
	private static final long serialVersionUID = -3410734904488958352L;

	public ModuleLoadException()
	{
		// Redundant
	}

	public ModuleLoadException(String message)
	{
		super(message);
	}

	public ModuleLoadException(Throwable cause)
	{
		super(cause);
	}

	public ModuleLoadException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
