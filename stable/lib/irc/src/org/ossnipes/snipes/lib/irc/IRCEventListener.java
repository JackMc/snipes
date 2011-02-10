package org.ossnipes.snipes.lib.irc;


/** The interface for receiving IRC events. Implementing this interface allows the Object to call {@link IRCBase#addEventListener(IRCEventListener)}
 * to start receiving events specified in the {@link #register()} method.
 */
public interface IRCEventListener
{
	public Event[] register();
	public void handleEvent(Event ev, EventArgs args);
}