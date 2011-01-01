package org.ossnipes.snipes.lib.irc;

public enum Event
{
	/** This event is triggered when the server sends us a PING command, or someone else does.
	 * This is handled in the {@link IRCBase#handleInternalEvent(Event, EventArgs)} method,
	 * but it can also be handled in the {@link IRCBase#handleEvent(Event, EventArgs)} method
	 * without impairing normal functionality.
	 * 
	 * Params (gotten with {@link EventArgs#getParam(String)}):
	 * server -- The server we're recieving a ping from (may also be a nickname, not sure though.)
	 * 
	 */
	IRC_PING,
	/** This event is triggered when a user sends a PRIVMSG to us or a channel we are in.
	 * This is a normal event, and is not handled by 
	 * {@link IRCBase#handleInternalEvent(Event, EventArgs)}.
	 * 
	 * Params (gotten with {@link EventArgs#getParam(String)}:
	 * from -- The person who sent the PRIVMSG.
	 * 
	 * 
	 */
	IRC_PRIVMSG,
	/** This event is triggered when we join and are notified of the topic.
	 * This is handled in the {@link IRCBase#handleInternalEvent(Event, EventArgs)} 
	 * method, but it can also be handled in the {@link IRCBase#handleEvent(Event, EventArgs)}
	 * method without impairing the normal functionality.
	 * 
	 * Params (gotten with {@link EventArgs#getParam(String)}:
	 * server -- The server that told us about it.
	 * channel -- The channel we were told about.
	 * topic -- The actual topic.
	 */
	IRC_JOIN_TOPIC,
	IRC_TOPIC
}
