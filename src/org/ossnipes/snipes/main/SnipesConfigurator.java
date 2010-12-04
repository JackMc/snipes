package org.ossnipes.snipes.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.ossnipes.snipes.utils.Configuration;
import org.ossnipes.snipes.utils.Constants;
import org.ossnipes.snipes.utils.SerializableConfigReader;
import org.ossnipes.snipes.utils.SerializableConfigWriter;

public class SnipesConfigurator {
	Scanner defScanner = new Scanner(System.in);
	Configuration conf = null;
	//TODO: Add empty String checker, still allows empty Strings as plugins.
	public SnipesConfigurator()
	{
		if (conf == null)
		{
			if (new File(Constants.CONFNAME).exists())
			{
				try
				{
					conf = new SerializableConfigReader(Constants.CONFNAME).readConf();
				} catch (Exception e) {System.err.println("Error while reading configuration, most likely you do not have the access to read from " + Constants.CONFNAME);}
			}
		}
		System.out.print("Snipes Configurator\n\n");
		System.out.println("This program will help you with configuring your Snipes.");
		System.out.print("Let's get down to business :)");
		System.out.println("PLEASE NOTE: To exit the process and save what you have currently configured, just type \"{SNIPESBOTSAVE}\" (had to be something someone wouldn't use as a value, tehe :))");
		System.out.println();
		System.out.println("Would you like to reset Snipes' plugins on load list back to the default core plugins? [Y or N (anything other than Y is N)?]");
		String x = defScanner.nextLine();
		checkForArgs(x);
		if (x.equalsIgnoreCase("Y"))
		{
			conf.plugins.clear();
			for (String ss : Constants.CORE_PLUGINS)
			{
				conf.plugins.add(ss);
				
			}
		}
		System.out.println();
		System.out.println("Would you like to edit what plugins are loaded on startup (this can be done while the bot is up with the \".addonloadp <pluginname>\" command private messaged to the bot (moderator rights required)) [Y/N]");
		String s = defScanner.nextLine();
		checkForArgs(s);
		if (s.equalsIgnoreCase("Y"))
		{
			setOnLoadPlugins();
		}
		try {
			SerializableConfigWriter.writeConfiguration(conf, Constants.CONFNAME);
		} catch (FileNotFoundException e) {
			System.err.println("You do not have permission to access " + Constants.CONFNAME);
		} catch (IOException e) {
			System.err.println("Unknown IOException encountered.");
		}
	}
	private void setOnLoadPlugins()
	{
		System.out.println();
		System.out.println();
		System.out.println("Please note: You can do {CHECKPLUGINS} at any time to list current on-load plugins.");
		
		System.out.println();
		System.out.println("Type the name of the plugins you would like to remove (if any, seperated by commas?)");
		String me = defScanner.nextLine();
		checkForArgs(me);
		String[] exx = me.split(",");
		if (exx.length != 0 && !exx[0].equalsIgnoreCase(""))
		{
			for (String plug : exx)
			{
				plug = plug.trim();
				for (int i = 0;i<conf.plugins.size();i++)
				{
					if (conf.plugins.get(i).contentEquals(plug))
					{
						conf.plugins.remove(i);
					}
				}
			}
		}
	}
	private boolean checkForArgs(String s)
	{
		if (s.equalsIgnoreCase("{SNIPESBOTSAVE}"))
		{
			try 
			{
				saveConfig();
				Thread.sleep(200);
			} catch (FileNotFoundException e) {
					System.err.println("You do not have permission to access " + Constants.CONFNAME);
				} catch (IOException e) {
					System.err.println("Unknown IOException encountered. Message was: " + e.getMessage());
				} catch (InterruptedException e) {/*Do we really care?*/}
				System.exit(0);
				return true;
		}
		if (s.equalsIgnoreCase("{CHECKPLUGINS}"))
		{
			System.out.println();
			for (String ss : conf.plugins)
			{
				System.out.println("\"" + ss + "\"");
			}
			System.out.println("End of list.");
			System.out.println();
			return true;
		}
		return false;
	}
	private void saveConfig() throws FileNotFoundException,IOException
	{
		SerializableConfigWriter.writeConfiguration(conf, Constants.CONFNAME);
	}
	
	private void askPlugAdd()
	{
		System.out.print("What plugins would you like to load on startup (if any, seperated by commas?)");
		System.out.println();
		String s = defScanner.nextLine();
		checkForArgs(s);
		String[] ex = s.split(",");
		if (ex.length != 0 && !ex[0].equalsIgnoreCase(""))
		{
			for (String plug : ex)
			{
				plug = plug.trim();
				conf.plugins.add(plug);
			}
		}
	}
	private boolean checkKeywords(String msg)
	{
		if (msg.equalsIgnoreCase("{CHECKPLUGINS}"))
		{
			System.out.println();
			for (String s : conf.plugins)
			{
				System.out.println("\"" + s + "\"");
			}
			System.out.println("End of list.");
			System.out.println();
			return true;
		}
		return false;
	}
}
