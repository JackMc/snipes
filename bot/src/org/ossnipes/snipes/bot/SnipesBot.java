package org.ossnipes.snipes.bot;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.ossnipes.snipes.lib.events.Event;
import org.ossnipes.snipes.lib.events.EventArgs;
import org.ossnipes.snipes.lib.events.IRCBase;

/** The main class for the SnipesBot project.
 * 
 * @author Jack McCracken */
public class SnipesBot extends IRCBase implements PropertyConstants,
		SnipesConstants
{
	/** Creates a new SnipesBot Object.
	 * 
	 * @param args The arguments passed into the program.
	 * @throws IOException If there is a error connecting to the specified
	 *             server. */
	public SnipesBot(String[] args) throws IOException
	{
		// Parse our passed in args.
		this.parseCmdArgs(args);

		// Initialise the configuration Object.
		this.initialiseConfiguration();

		// Get the nick, etc.
		this.readSetNickRealname();

		// See if we're in debugging/verbose mode.
		this.readSetDebugVerbose();

		try
		{
			// Try and read the server and port.
			this.readServerPortConnect();
		} catch (UnknownHostException e)
		{
			System.err.println("Unknown host specified in " + SERVER_PROP_NAME
					+ " property.");
			throw e;
		} catch (IOException e)
		{
			System.err
					.println("Unknown IOException while connecting to host specified in "
							+ SERVER_PROP_NAME + " property.");
			throw e;
		}

		// Join channels.
		this.readChannelsAndJoin();

		// Load modules.
		this.readLoadModules();
	}

	/** Reads the modules from the configuration and loads them */
	private void readLoadModules()
	{
		// Use a set so that duplicates are eliminated.
		// Concatenate the specified modules and the core ones.
		Set<String> temp = new HashSet<String>();
		temp.addAll(Arrays.asList(SnipesConstants.CORE_MODULES));
		temp.addAll(Arrays.asList(this._c.getPropertyAsStringArray(
				MODULES_PROP_NAME, MODULES_PROP_DEFAULTS)));
		// Stick it in a module collection.
		this._coll = new ModuleCollection(this, temp.toArray(new String[temp
				.size()]));
	}

	/** Reads and sets the nick and realname of the bot. */
	private void readSetNickRealname()
	{
		this.setNick(this._c.getProperty(NICK_PROP_NAME, NICK_PROP_DEFAULT));
		this.setRealname(this._c.getProperty(REALNAME_PROP_NAME,
				REALNAME_PROP_DEFAULT));
	}

	/** Reads and sets debugging and verbosity preferences */
	private void readSetDebugVerbose()
	{
		Boolean debugging = this._c.getPropertyAsBoolean(DEBUG_PROP_NAME,
				DEBUG_PROP_DEFAULT);
		this.setDebugging(debugging != null ? debugging : false);

		Boolean verbose = this._c.getPropertyAsBoolean(VERBOSE_PROP_NAME,
				VERBOSE_PROP_DEFAULT);
		this.setVerbose(verbose != null ? verbose : false);
	}

	/** Initialises the bot's configuration Object. */
	private void initialiseConfiguration()
	{
		try
		{
			this._c = new Configuration(SnipesConstants.CONFIGURATION_FILENAME);
		} catch (IOException e)
		{
			System.err.println("Could not load configuration file "
					+ SnipesConstants.CONFIGURATION_FILENAME);
			System.exit(Exit.EXIT_CONFIGNOLOAD.ordinal());
		}
	}

	/** Parses the command line arguments passed to the bot.
	 * 
	 * @param args The arguments to parse. */
	private void parseCmdArgs(String[] args)
	{
		// Call up the ArgumentParser to do it for us.
		ArgumentParser.getParser().parseArgs(this._c, args);
	}

	/** Reads the property values for server and port and connects */
	private void readServerPortConnect() throws IOException
	{
		// Get the port
		Integer port = this._c.getPropertyAsInteger(PORT_PROP_NAME,
				PORT_PROP_DEFAULT);

		if (port == null)
		{
			System.err.println(PORT_PROP_NAME
					+ " could not be parsed as a integer. Using default value "
					+ PORT_PROP_DEFAULT + ".");
		}
		// Use the connect method to try and connect to the server.
		this.connect(
				this._c.getProperty(SERVER_PROP_NAME, SERVER_PROP_DEFAULT),
				port != null ? port : IRC_DEFAULT_PORT);
	}

	/** Reads the property values for channels and joins them */
	private void readChannelsAndJoin()
	{
		String[] channels = this._c.getPropertyAsStringArray(
				CHANNELS_PROP_NAME, CHANNELS_PROP_DEFAULTS);

		for (String channel : channels)
		{
			try
			{
				this.join(channel);
			} catch (IllegalArgumentException e)
			{
				System.err.println("Invalid channel name '" + channel + "': "
						+ e.getMessage());
			}
		}

	}

	/** {@inheritDoc} SnipesBot does not do anything in this method. */
	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		// No event handling code.
	}

	// Configuration methods
	/** Gets the current {@link Configuration} {@link Object} of this bot.
	 * 
	 * @return The current {@link Configuration} Object. */
	public Configuration getConfiguration()
	{
		return this._c;
	}

	// End configuration methods

	// Module collection methods.
	/** Loads a module.
	 *
	 * @return The {@link ModuleManager} Object of the loaded {@link Module}
	 * @throws ModuleLoadException If the Module is not a instance of Module or
	 *             it's subclasses.
	 * @throws InstantiationException If the bot cannot instantiate a Object of
	 *             the specified Module. This generally happens when the class
	 *             is an array class, or an abstract class.
	 * @throws IllegalAccessException If we cannot call the constructor of the
	 *             Module for whatever reason.
	 * @throws ClassNotFoundException If the bot could not find the Module.
	 * @throws ModuleInitException If the module returned
	 *             {@link ModuleReturn#ERROR} from {@link Module#snipesInit()} */
	ModuleManager loadModule(String moduleName) throws ModuleLoadException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, ModuleInitException
	{
		return this._coll.addModule(this, moduleName);
	}

	/** Removes a module from the list of modules, and stops it from receiving
	 * further events. This will call snipesFini when it is uncommented.
	 * 
	 * @param moduleName The name of the module.
	 * @return True if we were able to remove the module. */
	boolean removeModule(String moduleName)
	{
		return this._coll.removeModule(moduleName, ModuleExitState.EXIT_UNLOAD);
	}

	/** Returns true if the specified module is loaded by this bot, and false
	 * otherwise.
	 * 
	 * @param name The name of the module to check the loaded status of.
	 * @return See above. */
	boolean isModuleLoaded(String name)
	{
		return this._coll.isModuleLoaded(name);
	}

	Module getModuleByName(String name)
	{
		return this._coll.getModuleByName(name);
	}

	@Override
	public void run()
	{
		if (this._coll == null)
		{
			// Nothing to see here...
			return;
		}
		for (ModuleManager mm : this._coll)
		{
			mm.destruct(ModuleExitState.EXIT_QUITTING);
		}
		super.run();
	}

	private Configuration _c;
	private ModuleCollection _coll;
}
