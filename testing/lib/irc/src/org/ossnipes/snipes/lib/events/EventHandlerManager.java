package org.ossnipes.snipes.lib.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class EventHandlerManager
{
	EventHandlerManager(IRCEventListener managed)
	{
		if (managed == null)
		{
			throw new IllegalArgumentException("Cannot manage null object.");
		}
		else
		{
			_managed = managed;
			_subscribedEvents = Collections.synchronizedList(new ArrayList<Event>());
			registerInitialEvents();
		}
	}

	/** Adds a event to the list of subscribed events.
	 * 
	 */
	void addEvent(Event ev)
	{
		if (_subscribedEvents.contains((Object)ev))
		{
			throw new IllegalArgumentException("Cannot add a Event already in the list.");
		}
		_subscribedEvents.add(ev);
	}
	
	/** Adds a event array to the list of subscribed events.
	 * 
	 */
	void addEvent(Event[] evs)
	{
		if (evs == null)
		{
			return;
		}
		for (Event ev : evs)
		{
			addEvent(ev);
		}
	}

	boolean removeEvent(Event ev)
	{
		if (!_subscribedEvents.contains((Object)ev))
		{
			return false;
		}
		else
		{
			_subscribedEvents.remove((Object)ev);
			return true;
		}
	}

	boolean isSubscribed(Event ev)
	{
		return _subscribedEvents.contains((Object)ev);
	}

	private Event[] populateSubscribedEvents() {
		_subscribedEvents.clear();
		Event[] registered = _managed.getRegisteredEvents();
		addEvent(registered == null ? new Event [] {} : registered);
		return registered;
	}

	void handleEvent(Event ev, EventArgs args)
	{
		_managed.handleEvent(ev, args);
	}

	boolean isIRCBase()
	{
		return _managed instanceof IRCBase;
	}

	IRCEventListener getManaged()
	{
		return _managed;
	}
	void sendEvent(Event ev, EventArgs args)
	{
		_managed.handleEvent(ev,args);
	}
	
	public Event[] registerInitialEvents()
	{
		Event[] registered = populateSubscribedEvents();
		return registered == null ? new Event [] {} : registered;
	}
	
	public Event[] getRegisteredEvents() {
		return _managed.getRegisteredEvents();
	}
	
	private List<Event> _subscribedEvents;
	private IRCEventListener _managed;
}