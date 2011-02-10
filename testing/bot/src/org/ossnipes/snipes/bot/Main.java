package org.ossnipes.snipes.bot;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main
{
	public static void main(String args[])
	{
		// Simple, for now :).
		try
		{
			new SnipesBot(args);
		} catch (UnknownHostException e)
		{
			System.err.println("ERROR: Unknown host.");
			System.exit(Exit.EXIT_COULDNOTCONNECT.ordinal());
		} catch (IOException e)
		{
			System.err.println("ERROR: Unknown IOException.");
			System.exit(Exit.EXIT_COULDNOTCONNECT.ordinal());
		}
	}
}
