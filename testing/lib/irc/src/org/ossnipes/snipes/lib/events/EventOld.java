package org.ossnipes.snipes.lib.events;

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

/** Used to represent a event,
 * @deprecated Doesn't do anything. Just here for backup.
 */
enum EventOld
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
	/** This event is triggered when a user joins a channel we are in.
	 * This is not handled in {@link IRCBase#handleInternalEvent(Event, EventArgs)}.
	 * <BR/><BR/>
	 * Params (gotten with {@link EventArgs#getParam(String)}:
	 * <BR/>
	 * nick -- The nick of the user joining the channel.<BR/>
	 * host -- The hostname of the user joining the channel.<BR/>
	 * channel -- The channel the user is joining.
	 */
	IRC_JOIN,
	/** This event is triggered when a user leaves a channel we are in.
	 * This is not handled in {@link IRCBase#handleInternalEvent(Event, EventArgs)}.
	 * <BR/><BR/>
	 * Params (gotten with {@link EventArgs#getParam(String)}:
	 * <BR/>
	 * nick -- The nick of the user leaving the channel</BR>
	 * host -- The hostname of the user leaving the channel.<BR/>
	 * channel -- The channel being left<BR/>
	 * message -- The part message of the user when leaving the channel. Generally, servers fill this in if
	 * it is not provided, but it is possible that it may send a PART without a message. In that case, the message
	 * parameter will be null.
	 */
	IRC_PART,
	/** This event is triggered when a user (possibly us) is kicked from a channel we are in.
	 * This is not handled in {@link IRCBase#handleInternalEvent(Event, EventArgs)}.
	 * <BR/><BR/>
	 * Params (gotten with {@link EventArgs#getParam(String)}):
	 * <BR/>
	 * kicker -- The nick of the person kicking the user from the channel.<BR/>
	 * kicker-host -- The hostname of the person kicking the user from the channel.<BR/>
	 * channel -- The channel the user is being kicked from.<BR/>
	 * kicked -- The nick of the user being kicked.<BR/><BR/>
	 * Possible bugs in this event:
	 * <BR/>
	 * - This event may not provide completely correct information if the command uses the comma-
	 * separated list syntax in this command. I have never seen a server do it, and I'm still
	 * attempting to think of a way to implement it. If you see a server that uses this syntax
	 * for it's kicks, please send me a email or tell me on IRC with the name and version of the
	 * server software sending the command to your client. (You can obtain this information through
	 * the use of a VERSION command on the server. You really only need the first line of this response.
	 * It should look something like "Unreal3.2.8-gs.9. Rubicon.GeekShed.net :FhiXeOoEmM3 [*=2309").
	 */
	IRC_KICK,
	/** This event is triggered when a user changes their nick on a channel we are in. The bot cannot provide
	 * channel information for this event, as it is only triggered once, no matter how many channels we
	 * have in common with the user.
	 * This is not handled in the {@link IRCBase#handleInternalEvent(Event, EventArgs)} method. This may change
	 * in another version though.
	 * <BR/><BR/>
	 * Params (gotten with {@link EventArgs#getParam(String)}:
	 * <BR/>
	 * nick-old -- The old nick of the user, before this event.<BR/>
	 * nick-new -- The new nick of the user, after this event.<BR/>
	 * host -- The hostname of the user changing their nick.
	 */
	IRC_NICK_CHANGE,
	/** This event is triggered when someone sends a notice to us or a channel we are in. This event functions almost
	 * exactly as the {@link #public static final EvNew IRC_PRIVMSG} event, but I will explain it's params here. This is not handled in the
	 * {@link IRCBase#handleInternalEvent(Event, EventArgs)} method.
	 * <BR/><BR/>
	 * Params (gotten with {@link EventArgs#getParam(String)}):
	 * <BR/>
	 * from -- The nick of the user sending the message.<BR/>
	 * from-host -- The hostname of the user sending the message.<BR/>
	 * to -- The nick of the user/channel this message is being sent to. This parameter will be the bot's nick
	 * if the message is directed to us only.<BR/>
	 * message -- The body of the message being sent to us.
	 */
	IRC_NOTICE,
	/** This event is triggered when someone quits in a channel we are in. The bot cannot provide channel information
	 * on this event because it is only sent to us once, no matter how many channels we have in common with the user.
	 * This event is not handled in the {@link IRCBase#handleInternalEvent(Event, EventArgs)} method. This may change
	 * if a further feature requires that we keep a constantly updated list of users in all channels we are in.
	 * <BR/><BR/>
	 * Params (gotten with {@link EventArgs#getParam(String)}):
	 * <BR/>
	 * nick -- The nick of the user quitting.<BR/>
	 * host -- The hostname of the user quitting.<BR/>
	 * message -- The quit message specified. As with the {@link #public static final EvNew IRC_PART} event, this parameter may be null.<BR/>
	 * This is because the server is not required to send a message along with the command (although it generally
	 * fills it in with something). The protocol does specify, however, that if servers detect that the connection
	 * was closed unexpectedly (the client did not send a QUIT message) then it must fill in the quit message with
	 * something appropriately describing the circumstances.
	 */
	IRC_QUIT,
	/** This event is triggered when a user or a server sets a mode on us or a channel we are in. I was going to
	 * implement the ability for events describing certain modes (a set moderated event, etc.) but that would add
	 * more to the size of the finished JAR (by added in extra checks, and documentation (docs only affecting the source
	 * packages). If you believe that it is worth it, I may add another "non-lightweight" version containing more events
	 * for simplifying the handling of others.)
	 * This event is not handled in the {@link IRCBase#handleInternalEvent(Event, EventArgs)} method.
	 * <BR/><BR/>
	 * Params (gotten with {@link EventArgs#getParam(String)}):
	 * <BR/>
	 * setter -- The person (or server) setting the mode.<BR/>
	 * setter-host -- If a user is setting the mode, then this is the host of the person setting the mode. If it is a
	 * server, then it is also the name of the server (copied from the "setter" parameter).<BR/>
	 * mode -- The actual String of + and - modes.<BR/>
	 * mode-params -- The list of parameters to the mode. This may be null if no parameters are specified in the command.
	 */
	IRC_MODE,
	/** This event is triggered when a line that the bot does not understand is sent to it. For example a non-standard
	 * statement or a part of the protocol I (embarrassingly) forgot to implement. This event is (obviously?) not handled
	 * in the {@link IRCBase#handleInternalEvent(Event, EventArgs)} method.
	 * <BR/><BR/>
	 * Params (gotten with {@link EventArgs#getParam(String)}):
	 * <BR/>
	 * line -- The raw line received from the server.
	 */
	IRC_UNKNOWN,
	/** This event is used in the bot as a method to get passed the limitation of not being able to retrieve Strings
	 * from the {@link java.net.Socket}'s buffer except for in the {@link IRCReceiver} class. Currently, this event is
	 * never sent to {@link IRCEventListener}s other than {@link IRCBase#handleInternalEvent(Event, EventArgs)},
	 *  as the handling of it in {@link IRCBase#handleInternalEvent(Event, EventArgs)} involves the application exiting.
	 * This may later be revised to allow bots to execute cleanup code when our nick is already in use.
	 * <BR/><BR/>
	 * Params (gotten with {@link EventArgs#getParam(String)}):<BR/>
	 * line -- The raw line received from the server. Currently not used.
	 */
	IRC_NICKINUSE
}
