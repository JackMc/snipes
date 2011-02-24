package plugins.noircman;

import org.ossnipes.snipes.bot.Configuration;
import org.ossnipes.snipes.bot.Module;
import org.ossnipes.snipes.bot.ModuleReturn;
import org.ossnipes.snipes.lib.irc.Event;
import org.ossnipes.snipes.lib.irc.EventArgs;
import org.ossnipes.snipes.lib.irc.IRCEventListener;

public class NoIRCModule extends Module
{

	@Override
	protected ModuleReturn snipesInit()
	{
		// TODO: Needs A LOT of work.
		new NoIRCMain(this);

		return null;
	}

	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		// No code
	}

	public Configuration getConf()
	{
		return this.getConfiguration();
	}

	public boolean isConnected()
	{
		return this.getParent().isConnected();
	}

	public void addEventListener(IRCEventListener evl)
	{
		this.getParent().addEventListener(evl);
	}
}
