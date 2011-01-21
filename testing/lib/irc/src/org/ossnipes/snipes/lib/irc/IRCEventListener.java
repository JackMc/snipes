package org.ossnipes.snipes.lib.irc;

public interface IRCEventListener
{
    public Event[] register();
    public void handleEvent(Event ev, EventArgs args);
}