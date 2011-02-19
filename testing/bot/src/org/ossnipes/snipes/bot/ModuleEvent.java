package org.ossnipes.snipes.bot;

import org.ossnipes.snipes.lib.irc.Event;

/** Will represent a event that has to do with the loading of modules.
 * 
 * @author Jack McCracken */
public class ModuleEvent extends Event
{
	protected ModuleEvent(String name)
	{
		// Create a normal event.
		super(name);
	}
}
