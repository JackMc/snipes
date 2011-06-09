package org.ossnipes.snipes.bot;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ossnipes.snipes.lib.irc.BotUtils;
import org.ossnipes.snipes.lib.irc.Event;
import org.ossnipes.snipes.lib.irc.EventArgs;
import org.ossnipes.snipes.lib.irc.IRCBase;

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

		this.addCommand(new TestCommand(this));

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
		// Concatenate the specified modules and the core ones.
		List<String> tempLst = new ArrayList<String>();
		tempLst.addAll(Arrays.asList(SnipesConstants.CORE_MODULES));
		tempLst.addAll(Arrays.asList(this._c.getPropertyAsStringArray(
				MODULES_PROP_NAME, MODULES_PROP_DEFAULTS)));
		// Stick it in a module collection.
		this._coll = new ModuleCollection(this,
				tempLst.toArray(new String[tempLst.size()]));
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
	private void readServerPortConnect() throws IOException,
			UnknownHostException
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
			// If it's less than two (it doesn't have a prefix or is just the
			// prefix)
			if (channel.length() < 2)
			{
				System.err
						.println("Channel joining: Cannot join "
								+ channel
								+ ". Channel name too short (must be the prefix and at least one other character.). Not joining...");
			}
			// TODO: Add into framework. This is IRC stuff not pertaining to
			// plugins.
			// See if it has a valid prefix.
			boolean validPrefix = false;
			// Get the first char of the channel
			char c = channel.charAt(0);
			// Loop through the valid prefixes.
			for (char prefix : IRC_CHANPREFIXES)
			{
				// If they have this prefix
				if (prefix == c)
				{
					validPrefix = true;
					// No sense in continuing checks.
					break;
				}
			}
			// Check if they gave us a valid channel prefix
			if (!validPrefix)
			{
				System.err
						.println("Channel joining: Could not join channel "
								+ channel
								+ ". Invalid or no prefix. Snipes will continue without joining this channel.");
			}
			else
			{
				this.join(channel);
			}
		}

	}

	/** {@inheritDoc} SnipesBot does not do anything in this method. */
	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		String message, sendTo;
		String[] split;
		boolean totalMatchFound = false;
		boolean invalidArgsMatchFound = false;

		if (this._commands.size() != 0
				&& (message = args.getParamAsString("message")) != null
				&& (sendTo = args.getParamAsString("sendto")) != null
				&& (split = message.split(" ")).length != 0)
		{
			for (Command c : BotUtils.copySet(this._commands))
			{
				int argsCount = c.getNumberOfArgs();
				String command = c.getCommand();
				if (command != null && command.equalsIgnoreCase(split[0]))
				{
					if (argsCount != split.length - 1)
					{
						this.debug("Commands API: Found a command for "
								+ command
								+ " but it doesn't have "
								+ "the correct amount of arguments as provided ("
								+ argsCount + " != " + split.length + ".)");
						invalidArgsMatchFound = true;
						continue;
					}
					else
					{
						this.debug("Commands API: Found a match for command "
								+ command + " with " + argsCount + " args.");
						c.handleCommand(message, sendTo, split);
						totalMatchFound = true;
						return;
					}
				}
			}

			if (invalidArgsMatchFound && !totalMatchFound)
			{
				this.sendPrivMsg(sendTo,
						"Incorrect amount of arguments for command " + split[0]
								+ ".");
			}

		}
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
	 * @param module The module to load.
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

	// End module collection methods

	public Command addCommand(Command c)
	{
		if (this._commands == null)
		{
			this._commands = new HashSet<Command>();
		}

		for (Command cLooped : this._commands)
		{
			if (!cLooped.getCommand().equals(c.getCommand())
					&& cLooped.getNumberOfArgs() == c.getNumberOfArgs())
			{
				System.err
						.println("COMMAND CONFLICT! Different commands with the name "
								+ cLooped.getCommand()
								+ " and "
								+ cLooped.getNumberOfArgs()
								+ " argument"
								+ (cLooped.getNumberOfArgs() == 1 ? "" : "s")
								+ ". Snipes will continue using "
								+ "the first one added.");
				return c;
			}
		}

		this._commands.add(c);
		return c;
	}

	private Configuration _c;
	private ModuleCollection _coll;
	private Set<Command> _commands;
}
