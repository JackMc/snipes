package org.ossnipes.snipes.lib.irc;

import java.util.List;
import java.util.ArrayList;

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
            _subscribedEvents = new ArrayList<Event>();
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
    
    private List<Event> _subscribedEvents;
    private IRCEventListener _managed;
}