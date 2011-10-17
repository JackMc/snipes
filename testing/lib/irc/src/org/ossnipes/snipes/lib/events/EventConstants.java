package org.ossnipes.snipes.lib.events;

public interface EventConstants 
{
	/** The set of events that need handling by the bot framework itself, along with possibly 
	 * the user.
	 */
	static final Event[] INTERNAL_EVENTS = { Event.IRC_JOIN, Event.IRC_NICKINUSE };
}
