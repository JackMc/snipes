package org.ossnipes.snipes.lib.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventHandlerCollection {
	public EventHandlerCollection() {
		_evmngrs = new ArrayList<EventHandlerManager>();
		_pool = Executors.newCachedThreadPool();
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
	
	public void setThreadLevel(ThreadLevel tl)
	{
		_threadLevel = tl;
	}
	
	public ThreadLevel getThreadLevel()
	{
		return _threadLevel;
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
	
	ThreadLocal<Event> getCurrentEventTl()
	{
		return _currentEvent;
	}
	
	ExecutorService getThreadPool()
	{
		return _pool;
	}
	
	List<EventHandlerManager> getListeners() 
	{
		return _evmngrs;
	}
	
	private List<EventHandlerManager> _evmngrs;
	private ThreadLocal<Event> _currentEvent = new ThreadLocal<Event>();
	private ExecutorService _pool = null;
	private ThreadLevel _threadLevel = ThreadLevel.TL_PER_HANDLER;
}
