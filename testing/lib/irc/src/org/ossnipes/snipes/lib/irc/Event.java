package org.ossnipes.snipes.lib.irc;

/* 
 * 
 * Copyright 2010 Jack McCracken
 * This file is part of The Snipes IRC Framework.
 * 
 * The Snipes IRC Framework is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * The Snipes IRC Framework is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * Although this project was created for use within The Open Source Snipes Project, it is legally 
 * a completely different project. This means that you may distribute it (along with 
 * the source) with your own GPL-compatible projects without having to distribute the actual
 * Implementation project (The Open Source Snipes Project) with it.
 * 
 * Note for other developers of this project: Please include this in any files you create, so that it
 * may be made "legally" a part of the project.
 * 
 * You should have received a copy of the GNU General Public License along with The Snipes IRC Framework. 
 * If not, see http://www.gnu.org/licenses/.
 */

public enum Event
{
	/** This event is triggered when the server sends us a PING command, or someone else does.
	 * This is handled in the {@link IRCBase#handleInternalEvent(Event, EventArgs)} method,
	 * but it can also be handled in the {@link IRCBase#handleEvent(Event, EventArgs)} method
	 * without impairing normal functionality.
	 * 
	 * <BR/><BR/>Params (gotten with {@link EventArgs#getParam(String)}):<BR/>
	 * server -- The server we're recieving a ping from (may also be a nickname, not sure though.)
	 * 
	 */
	IRC_PING,
	/** This event is triggered when a user sends a PRIVMSG to us or a channel we are in.
	 * This is a normal event, and is not handled by 
	 * {@link IRCBase#handleInternalEvent(Event, EventArgs)}.
	 * 
	 * <BR/><BR/>Params (gotten with {@link EventArgs#getParam(String)}):<BR/>
	 * from -- The person who sent the PRIVMSG.<BR/>
	 * from-host -- The host of the person sending the PRIVMSG.<BR/>
	 * to -- The nickname/channel the PRIVMSG is being sent to. If it is a message only to us, 
	 * to will equal {@link IRCBase#getNick()}<BR/>
	 * message -- The actual message.
	 */
	IRC_PRIVMSG,
	/** This event is triggered when we join and are notified of the topic.
	 * This is handled in the {@link IRCBase#handleInternalEvent(Event, EventArgs)} 
	 * method, but it can also be handled in the {@link IRCBase#handleEvent(Event, EventArgs)}
	 * method without impairing the normal functionality.
	 * 
	 * <BR/><BR/>Params (gotten with {@link EventArgs#getParam(String)}):<BR/>
	 * server -- The server that told us about it.<BR/>
	 * channel -- The channel we were told about.<BR/>
	 * topic -- The actual topic.<BR/>
	 */
	IRC_JOIN_TOPIC,
	/** This event is triggered when we get notified that the topic has changed.
	 * This is handled in the {@link IRCBase#handleInternalEvent(Event, EventArgs)} 
	 * method, but it can also be handled in the {@link IRCBase#handleEvent(Event, EventArgs)}
	 * method without impairing normal functionality.
	 * 
	 * <BR/><BR/>Params (gotten with {@link EventArgs#getParam(String)}):<BR/>
	 * setter -- The nick of the person setting the topic.<BR/>
	 * setter-host -- The hostname of the person setting the topic.<BR/>
	 * channel -- The channel the topic was set on.<BR/>
	 * topic -- The text of the topic.<BR/>
	 */
	IRC_TOPIC,
    /** This event is triggered when we get sent any IRC response code.
    * This may be any of the things in IRCConstants.
    * This is not handled by {@link IRCBase#handleInternalEvent(Event, EventArgs)}
    * method.<BR/><BR/>
    * 
    * Params (gotten with {@link EventArgs#getParam(String)}):
    * <BR/>
    * code: The numeric representation of the sent code. Matches with a value in
    * the {@link IRCConstants} class. This is of type {@link Integer}.
    * <BR/>
    * text: The text of the response.
    * <BR/>
    * server: The server sending the code.
    */
    IRC_RESPONSE_CODE,
    //TODO: Doc these events
    IRC_JOIN,
    IRC_PART,
    IRC_KICK,
    IRC_NICK_CHANGE,
    IRC_MODE,
    IRC_UNKNOWN
}
