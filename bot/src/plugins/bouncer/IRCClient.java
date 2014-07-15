package plugins.bouncer;

import org.ossnipes.snipes.bot.SnipesBot;

public class IRCClient implements PseudoClient
{
	// TODO: Better name. IRCClient will probably get confusing.
	public IRCClient(@SuppressWarnings("unused") SnipesBot sb)
	{
		// No need for Snipes object.
	}

	@Override
	public String getNick()
	{
		return null;
	}

	@Override
	public void performCommand(String command, BouncerConnection bc)
	{
		String[] split = command.split(" ");

		if (split.length >= 2)
		{
			// Special handing of stuff that might not mean to get to the server.
			if (split[0].equalsIgnoreCase("NICK") && _firstNick)
			{
				_firstNick = false;
				bc.fixClientNick(split[1]);
				return;
			}
			if (split[0].equalsIgnoreCase("USER"))
			{
				return;
			}
			if (split[1].equalsIgnoreCase("QUIT"))
			{
				bc.closeConnection();
			}
		}
		
		bc.sendRawLineToServer(command);
	}

	@Override
	public boolean isLineTo(String line, BouncerConnection bc)
	{
		return true;
	}

	boolean _firstNick = true;
}
