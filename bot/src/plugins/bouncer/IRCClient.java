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
		System.err.println("Command fell through to the IRCClient.");
		bc.sendRawLineToServer(command);
	}

	@Override
	public boolean isLineTo(String line, BouncerConnection bc)
	{
		return true;
	}
}