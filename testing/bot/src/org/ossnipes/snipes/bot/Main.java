package org.ossnipes.snipes.bot;

import java.io.IOException;

public class Main
{
	public static void main(String args[])
	{
		// Simple, for now :).
		try
		{
			checkDeps();
			new SnipesBot(args);
		} catch (IOException e)
		{
			System.err.println("SnipesBot: Connection error ("
					+ e.getClass().getCanonicalName() + ") thrown: "
					+ e.getMessage());
			System.exit(Exit.EXIT_COULDNOTCONNECT.ordinal());
		}
	}

	/**
	 * Checks the needed dependencies. This is in {@link Main} because it allows
	 * the bot to detect a missing Snipes IRC Framework and quit cleanly with a
	 * nice error without the tons of {@link ClassNotFoundException}s.
	 */
	private static void checkDeps()
	{
		// TODO HACK: Is there a way to see if a class exists without loading
		// it?
		try
		{
			// To test if they have the collections API (A feature added in Java
			// 1.2).
			Class.forName("java.util.Collection");
		} catch (ClassNotFoundException e)
		{
			System.err
					.println("Checks have shown that you do not have the Java Collections API. This API is required to run Snipes."
							+ " The Java Collections API has existed in the Java standrad library since Java version 1.2, and you are running version "
							+ System.getProperty("java.version")
							+ ". Please upgrade Java and try again.");
			System.exit(Exit.EXIT_JVM_VERSION_TOO_LOW.ordinal());
		}
		try
		{
			// To test if they have the collections API (A feature added in Java
			// 1.2).
			Class.forName("org.ossnipes.snipes.lib.events.IRCBase");
		} catch (ClassNotFoundException e)
		{
			System.err
					.println("Checks have shown that you do not have the Snipes IRC API. This is built into most Snipes releases. Please try redownloading "
							+ "if you did not build this from source. If the problem persists, contact the developer at "
							+ SnipesConstants.DEV_EMAIL
							+ ". If you built this copy from source, please make sure you either used Eclipse's jar export"
							+ " function, or make sure that the Snipes IRC Library is in your CLASSPATH.");
			System.exit(Exit.EXIT_NO_SNIRC.ordinal());
		}
	}
}
