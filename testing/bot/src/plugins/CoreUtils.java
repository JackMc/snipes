package plugins;

import org.ossnipes.snipes.bot.Module;
import org.ossnipes.snipes.bot.PluginExitState;
import org.ossnipes.snipes.bot.PluginReturn;
import org.ossnipes.snipes.lib.irc.Event;
import org.ossnipes.snipes.lib.irc.EventArgs;

public class CoreUtils extends Module
{

	@Override
	public Event[] register()
	{
		// TODO Auto-generated method stub
		return new Event[] {};
	}

	@Override
	protected PluginReturn snipesInit()
	{
		System.err.println("Hello :)");
		return null;
	}

	@Override
	protected void snipesFini(PluginExitState status)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		// TODO Auto-generated method stub

	}

	// TODO: Docs.
}
