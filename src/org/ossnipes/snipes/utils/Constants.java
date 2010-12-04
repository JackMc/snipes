package org.ossnipes.snipes.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Snipes constants. Not really much to say.
 * @author Jack McCracken (Auv5)
 *
 */

public class Constants {
	public static final String LOGGERNAME = "SnipesDefLogger";
	public static final String LOGNAME = "Snipes.log";
	// Use us events (currently causes reccursion)
	public static final boolean US_ENABLED = false;
	public static final PrintStream systemOutDef = System.out;
	public static final String CONFNAME = System.getProperty("user.home") + File.separator + ".snipes" + File.separator + "config.dat";
	public static final String[] CORE_PLUGINS = {"plugins.SPFManager"};
	
	
	public static PrintStream getDevNull() throws FileNotFoundException
	{
		if (System.getProperty("os.name").contains("Linux") || System.getProperty("os.name").contains("Mac"))
		{
			return new PrintStream(new File("/dev/null"));
		}
		else if (System.getProperty("os.name").contains("Windows"))
		{
			return new PrintStream(new File("NUL"));
		}
		return null;
	}
}
