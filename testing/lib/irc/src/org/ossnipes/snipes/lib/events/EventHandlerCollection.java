package org.ossnipes.snipes.lib.events;

import java.util.ArrayList;
import java.util.List;

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
			EventHandlerManager ehm = new EventHandlerManager(listener);
			ehm.registerInitialEvents();
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
	
	List<EventHandlerManager> getListeners() 
	{
		return _evmngrs;
	}
	
	private List<EventHandlerManager> _evmngrs;
}
