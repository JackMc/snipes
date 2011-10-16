package plugins.bouncer;

import org.ossnipes.snipes.bot.Module;
import org.ossnipes.snipes.bot.ModuleReturn;
import org.ossnipes.snipes.lib.events.Event;
import org.ossnipes.snipes.lib.events.EventArgs;

public class BouncerModule extends Module
{

	@Override
	protected ModuleReturn snipesInit()
	{
		// Optimise by removing ourselves as a candidate for event sending.
		this.getParent().removeEventListener(this);
		new BouncerMain(this.getParent());
		return null;
	}

	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		// Nothing yet.
	}
}
