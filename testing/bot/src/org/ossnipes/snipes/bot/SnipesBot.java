package org.ossnipes.snipes.bot;

import java.io.IOException;
import java.net.UnknownHostException;

import org.ossnipes.snipes.lib.irc.*;

public class SnipesBot extends IRCBase implements PropertyConstants
{
	Configuration c;

	public SnipesBot(String[] args) throws UnknownHostException, IOException
	{
		this.parseCmdArgs(args);

		this.initializeConfiguration();

		this.readSetNickRealname();

		this.readSetDebugVerbose();

		this.readServerPortConnect();

		this.readChannelsAndJoin();
	}

	private void readSetNickRealname()
	{
		// We can't specify a default in the constants, because this is
		// dependent on the bot's current nick.
		this.setNick(this.c.getProperty(NICK_PROP_NAME, NICK_PROP_DEFAULT));

		this.setRealname(this.c.getProperty(REALNAME_PROP_NAME,
				REALNAME_PROP_DEFAULT));
	}

	private void readSetDebugVerbose()
	{
		Boolean debugging = this.c.getPropertyAsBoolean(DEBUG_PROP_NAME,
				DEBUG_PROP_DEFAULT);
		this.setDebugging(debugging != null ? debugging : false);

		Boolean verbose = this.c.getPropertyAsBoolean(VERBOSE_PROP_NAME,
				VERBOSE_PROP_DEFAULT);
		this.setVerbose(verbose != null ? verbose : false);
	}

	private void initializeConfiguration()
	{
		try
		{
			this.c = new Configuration(Constants.CONFIGURATION_LOCATION);
		} catch (IOException e)
		{
			System.err.println("Could not load configuration file "
					+ Constants.CONFIGURATION_LOCATION);
			System.exit(Exit.EXIT_CONFIGNOLOAD.ordinal());
		}
	}

	private void parseCmdArgs(String[] args)
	{
		ArgumentParser.getParser().parseArgs(this.c, args);
	}

	private void readServerPortConnect() throws IOException,
			UnknownHostException
	{
		Integer port = this.c.getPropertyAsInteger(PORT_PROP_NAME,
				PORT_PROP_DEFAULT);

		if (port == null)
		{
			System.err.println(PORT_PROP_NAME
					+ " could not be parsed as a integer. Using default value "
					+ PORT_PROP_DEFAULT + ".");
		}

		this.connect(this.c.getProperty(SERVER_PROP_NAME, SERVER_PROP_DEFAULT),
				port != null ? port : IRC_DEFAULT_PORT);
	}

	private void readChannelsAndJoin()
	{
		String[] channels = this.c.getPropertyAsStringArray("channel",
				CHANNELS_PROP_DEFAULTS);

		for (String channel : channels)
		{
			if (channel.length() < 2)
			{
				System.err
						.println("Channel joining: Cannot join "
								+ channel
								+ ". Channel name too short (must be the prefix and at least one other character.). Not joining...");
			}

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

	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		// TODO: Event sending code.
	}
}
