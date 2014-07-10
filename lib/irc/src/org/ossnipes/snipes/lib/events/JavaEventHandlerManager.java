package org.ossnipes.snipes.lib.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ossnipes.snipes.lib.irc.SnipesException;

class JavaEventHandlerManager implements EventHandlerManager
{
	JavaEventHandlerManager(IRCEventListener managed)
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

	/* (non-Javadoc)
	 * @see org.ossnipes.snipes.lib.events.EventHandlerManager#addEvent(org.ossnipes.snipes.lib.events.Event)
	 */
	@Override
	public void addEvent(Event ev)
	{
		if (_subscribedEvents.contains((Object)ev))
		{
			throw new IllegalArgumentException("Cannot add a Event already in the list.");
		}
		_subscribedEvents.add(ev);
	}
	
	/* (non-Javadoc)
	 * @see org.ossnipes.snipes.lib.events.EventHandlerManager#addEvent(org.ossnipes.snipes.lib.events.Event[])
	 */
	@Override
	public void addEvent(Event[] evs)
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

	/* (non-Javadoc)
	 * @see org.ossnipes.snipes.lib.events.EventHandlerManager#removeEvent(org.ossnipes.snipes.lib.events.Event)
	 */
	@Override
	public boolean removeEvent(Event ev)
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

	/* (non-Javadoc)
	 * @see org.ossnipes.snipes.lib.events.EventHandlerManager#isSubscribed(org.ossnipes.snipes.lib.events.Event)
	 */
	@Override
	public boolean isSubscribed(Event ev)
	{
		return _subscribedEvents.contains((Object)ev);
	}

	private Event[] populateSubscribedEvents() {
		_subscribedEvents.clear();
		Event[] registered = _managed.getRegisteredEvents();
		addEvent(registered == null ? new Event [] {} : registered);
		return registered;
	}

	/* (non-Javadoc)
	 * @see org.ossnipes.snipes.lib.events.EventHandlerManager#handleEvent(org.ossnipes.snipes.lib.events.Event, org.ossnipes.snipes.lib.events.EventArgs)
	 */
	@Override
	public void handleEvent(Event ev, EventArgs args)
	{
		_managed.handleEvent(ev, args);
	}

	/* (non-Javadoc)
	 * @see org.ossnipes.snipes.lib.events.EventHandlerManager#isIRCBase()
	 */
	@Override
	public boolean isIRCBase()
	{
		return _managed instanceof IRCBase;
	}

	/* (non-Javadoc)
	 * @see org.ossnipes.snipes.lib.events.EventHandlerManager#handleInternalEvent(org.ossnipes.snipes.lib.events.Event, org.ossnipes.snipes.lib.events.EventArgs)
	 */
	@Override
	public void handleInternalEvent(Event ev, EventArgs args)
	{
		if (!isIRCBase())
		{
			throw new SnipesException("Cannot handle internal events on a non-IRCBase object.");
		}
		else
		{
			((IRCBase)_managed).handleInternalEvent(ev, args);
		}
	}
	/* (non-Javadoc)
	 * @see org.ossnipes.snipes.lib.events.EventHandlerManager#sendEvent(org.ossnipes.snipes.lib.events.Event, org.ossnipes.snipes.lib.events.EventArgs)
	 */
	@Override
	public void sendEvent(Event ev, EventArgs args)
	{
		_managed.handleEvent(ev,args);
	}
	
	/* (non-Javadoc)
	 * @see org.ossnipes.snipes.lib.events.EventHandlerManager#registerInitialEvents()
	 */
	@Override
	public Event[] registerInitialEvents()
	{
		Event[] registered = populateSubscribedEvents();
		return registered == null ? new Event [] {} : registered;
	}
	
	/* (non-Javadoc)
	 * @see org.ossnipes.snipes.lib.events.EventHandlerManager#getRegisteredEvents()
	 */
	@Override
	public Event[] getRegisteredEvents() {
		return _managed.getRegisteredEvents();
	}
	
	public boolean equals(Object o)
	{
		if (o instanceof IRCEventListener)
		{
			return _managed.equals(o);
		}
		else
		{
			return super.equals(o);
		}
	}
	
	private List<Event> _subscribedEvents;
	private IRCEventListener _managed;
}