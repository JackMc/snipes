package plugins.noircman;

import org.ossnipes.snipes.bot.Configuration;
import org.ossnipes.snipes.bot.Module;
import org.ossnipes.snipes.bot.ModuleReturn;
import org.ossnipes.snipes.bot.SnipesBot;
import org.ossnipes.snipes.lib.events.Event;
import org.ossnipes.snipes.lib.events.EventArgs;
import org.ossnipes.snipes.lib.events.IRCEventListener;

public class NoIRCModule extends Module
{

	@Override
	protected ModuleReturn snipesInit()
	{
		new NoIRCMain(this);
		// No use wasting time on us when we don't handle any events.
		this.getParent().removeEventListener(this);
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

	public SnipesBot getBot()
	{
		return this.getParent();
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
