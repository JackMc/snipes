package org.ossnipes.snipes.lib.irc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventHandlerCollection {
	public EventHandlerCollection() {
		_evmngrs = new ArrayList<EventHandlerManager>();
	}
	
	/** Adds a listener for events from the bot.
	 * @param listener The IRCEventListener
	 * @return The passed event listener, for convenience.
	 */
	public final IRCEventListener addEventListener(IRCEventListener listener)
	{
		if (listener == null)
		{
			throw new IllegalArgumentException("Cannot add null event handler.");
		}
		
		else if (_evmngrs.contains(listener))
		{
			throw new IllegalArgumentException("Event handler already added.");
		}
		else
		{
			debug("Added event listener: " + listener.getClass().getName() + 
					". This is #" + (_evmngrs.size() + 1) + ".");
			EventHandlerManager ehm = new EventHandlerManager(listener);
			for (Event e : listener.register())
			{
				ehm.addEvent(e);
			}
			_evmngrs.add(ehm);
			return listener;
		}
	}
	
	public final void removeEventListener(IRCEventListener listener)
	{
		if (listener == null)
		{
			throw new IllegalArgumentException("Cannot remove null event handler.");
		}
		else
		{
			List<EventHandlerManager> mans = this.getListeners();
			
			int i = 0;
			
			// Loop through the listeners
			while (i < mans.size())
			{
				EventHandlerManager ehm = mans.get(i);
				if (ehm.getManaged() == listener)
				{
					mans.remove(i);
				}
				i++;
			}
		}
	}
	
	private void debug(String s)
	{
		if (BotOptions.DEBUG)
			_logger.log(Level.INFO, s);
	}
	
	List<EventHandlerManager> getListeners() 
	{
		return _evmngrs;
	}
	
	private List<EventHandlerManager> _evmngrs;
	private static final Logger _logger = Logger.getLogger(EventHandlerCollection.class.getCanonicalName());
}
