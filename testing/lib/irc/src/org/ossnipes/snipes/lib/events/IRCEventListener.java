package org.ossnipes.snipes.lib.events;


/** The interface for receiving IRC events. Implementing this interface allows the Object to call {@link IRCBase#addEventListener(IRCEventListener)}
 * to start receiving events specified in the {@link #getRegisteredEvents()} method.
 */
public interface IRCEventListener
{
	public Event[] getRegisteredEvents();
	public void handleEvent(Event ev, EventArgs args);
}