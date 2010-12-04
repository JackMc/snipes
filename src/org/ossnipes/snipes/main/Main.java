package org.ossnipes.snipes.main;

import java.io.IOException;

import org.ossnipes.snipes.bot.SnipesBot;
import org.ossnipes.snipes.tests.TestMain;
import org.ossnipes.snipes.utils.SnipesConfigurator;


public class Main {
	public static void main(String args[])
	{
		boolean CONF = false;
		boolean TEST = false;
		for (String i : args)
		{
			if (!TEST && (i.equalsIgnoreCase("--makeconf") || i.equalsIgnoreCase("-m")))
			{
				CONF = true;
				new SnipesConfigurator();
			}
			if (!CONF && (i.equalsIgnoreCase("--test") || i.equalsIgnoreCase("-m")))
			{
				try {
					new TestMain();
					TEST = true;
				} catch (IOException e) {
					System.err.println("An unknown I/O error has occured while running Snipes' tests. The error message was: " + e.getMessage());
				} catch (ClassNotFoundException e) {
					System.err.println("An ClassNotFoundException occured while running Snipes' tests. The error message was: " + e.getMessage());
				}
			}
		}
		if (!CONF && !TEST)
			new SnipesBot();
	}
}
