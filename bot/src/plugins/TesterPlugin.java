package plugins;

import org.ossnipes.snipes.bot.Module;
import org.ossnipes.snipes.bot.ModuleReturn;
import org.ossnipes.snipes.lib.events.Event;
import org.ossnipes.snipes.lib.events.EventArgs;

public class TesterPlugin extends Module
{

	@Override
	protected ModuleReturn snipesInit()
	{
		System.err.println(":) I'm loaded.");
		return null;
	}

	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		System.out.println("Herro!");
	}
}
