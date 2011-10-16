package org.ossnipes.snipes.lib.events;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.net.SocketFactory;

import org.ossnipes.snipes.lib.irc.BotConstants;
import org.ossnipes.snipes.lib.irc.IRCSocketManager;
import org.ossnipes.snipes.lib.irc.SnipesException;
import org.ossnipes.snipes.lib.irc.SnipesSSLSocketFactory;

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

/**
 * Main class for the Snipes IRC framework. The Snipes IRC framework is a
 * framework that is meant to replace the current implementation of IRC
 * communication in The Open Source Snipes Project (PircBot) so that the project
 * can "stand on it's own two feet" and be able to change from license to
 * license as the author sees fit. There was also a need for SSL support, so
 * that is built into the framework with the {@link SnipesSSLSocketFactory}
 * class.
 * 
 * @author Jack McCracken (<a
 *         href="http://ossnipes.org/">http://ossnipes.org</a>)
 * 
 * @since Snipes 0.6
 * 
 */
public abstract class IRCBase extends IRCSocketManager implements IRCEventListener
{
	// Default constructor
	public IRCBase()
	{
		if (CHANNEL_TRACKING)
			_channels = new ArrayList<Channel>();
		_eventcoll = new EventHandlerCollection();
		_eventcoll.addEventListener(this);
	}

	/**
	 * Registers this class for listening of events.
	 * This does not apply to us, as we are a IRCBase,
	 * given extra power by the event sending mechanism
	 * to "enforce the rules" of who gets what events sent
	 * to them.
	 * 
	 * This method is never called, and is thus empty.
	 */
	public final Event[] getRegisteredEvents()
	{
		return new Event[] {};
	}

	public IRCEventListener addEventListener(IRCEventListener h)
	{
		return _eventcoll.addEventListener(h);
	}
	public void removeEventListener(IRCEventListener h)
	{
		_eventcoll.removeEventListener(h);
	}
	
	/**
	 * Connects to the IRC server.
	 * 
	 * @param server
	 *            The server IP/host to connect to. If null, a
	 *            {@link IllegalArgumentException} is thrown.
	 * @param port
	 *            The port to connect on. If it is not between 1 and 65535,
	 *            throws {@link IllegalArgumentException}.
	 * @param factory
	 *            The SocketFactory to use. If null, a SnipesSocketFactory is
	 *            used.
	 * @throws IOException
	 *             If there is a unknown IO error while connecting to the
	 *             server.
	 * @throws UnknownHostException
	 *             If the host specified by "server" doesn't exist.
	 */
	public IRCSocketManager connect(String server, int port, String passwd, SocketFactory factory)
	throws IOException, UnknownHostException
	{
		return connect(new IRCInputHandler(this), server, port, passwd, factory);
	}
	
	public IRCSocketManager connect(String server, int port, SocketFactory factory)
	throws IOException, UnknownHostException
	{
		return connect(server, port, null, factory);
	}
	
	/**
	 * Connects to the IRC server using a SnipesSocketFactory.
	 * 
	 * @param server
	 *            The server IP/host to connect to. If null, a
	 *            {@link IllegalArgumentException} is thrown.
	 * @param port
	 *            The port to connect on. If it is not between 1 and 65535,
	 *            throws {@link IllegalArgumentException}.
	 * @throws IOException
	 *             If there is a unknown IO error while connecting to the
	 *             server.
	 * @throws UnknownHostException
	 *             If the host specified by "server" doesn't exist.
	 * @see #connect(String, int, SocketFactory)
	 */
	public IRCSocketManager connect(String server, int port) throws IOException,
	UnknownHostException
	{
		// Defaults to a SnipesSocketFactory if third parameter is null.
		return connect(server, port, null);
	}
	
	/**
	 * Connects to the IRC server using a SnipesSocketFactory and the default
	 * port (see below.)
	 * 
	 * @param server
	 *            The server IP/host to connect to. If null, a
	 *            {@link IllegalArgumentException} is thrown.
	 * @throws IOException
	 *             If there is a unknown IO error while connecting to the
	 *             server.
	 * @throws UnknownHostException
	 *             If the host specified by "server" doesn't exist.
	 * @see #connect(String, int, SocketFactory)
	 * @see BotConstants#IRC_DEFAULT_PORT for the port used by default by
	 *      Snipes. I won't specify it here, as it may change.
	 */
	public IRCSocketManager connect(String server) throws IOException, UnknownHostException
	{
		// Connect to the server with the default IRC port and 
		// SocketFactory.
		return connect(server, IRC_DEFAULT_PORT, null);
	}
	
	public Channel[] getJoinedChannels()
	{
		if (!CHANNEL_TRACKING)
		{
			throw new UnsupportedOperationException("The API was compiled with CHANNEL_TRACKING set to false. Channel tracking features are disabled.");
		}
		return _channels.toArray(new Channel[_channels.size()]);
	}
	
	public Channel getChannelForName(String name)
	{
		Channel result = null;
		
		for (Channel c : _channels)
		{
			// IRC's channel naming is case-insensitive.
			if (c.getName().equalsIgnoreCase(name))
			{
				return c;
			}
		}
		
		return result;
	}
	
	/** Used to handle a event sent by {@link #sendEvent(Event, EventArgs)}.
	 * @param ev The event that was sent.
	 * @param args The arguments for the event.
	 */
	public abstract void handleEvent(Event ev, EventArgs args);

	/** This method is called when a event in the {@link BotConstants#INTERNAL_EVENTS} array
	 * is triggered.
	 * @param ev The event that was sent.
	 * @param args The arguments for the event.
	 */
	public final void handleInternalEvent(Event ev, EventArgs args)
	{
		if (ev == Event.IRC_PING)
		{
			sendPong(args.getParamAsString("server"));
		}
		else if (ev == Event.IRC_JOIN)
		{
			if (CHANNEL_TRACKING)
			{
				// We're only interested if the nick is us.
				if (args.getParamAsString("nick").equalsIgnoreCase(getNick()))
				{
					_channels.add((Channel)addEventListener(new Channel(args.getParamAsString("channel"))));
				}
			}
		}
		else if (ev == Event.IRC_PART)
		{
			if (CHANNEL_TRACKING)
			{
				if (args.getParamAsString("nick").equalsIgnoreCase(getNick()))
				{
					String chanName = args.getParamAsString("channel");
					
					// Returns false if the Object did not exist in the first place.
					if (!_channels.remove(getChannelForName(chanName)))
					{
						// We didn't have it in our lists? But we were parting it!
						throw new SnipesException("Channel was not in the lists, yet we parted it.");
					}
				}
			}
		}
		else if (ev == Event.IRC_NICKINUSE && (Boolean)args.getParam("fatal"))
		{
				System.err.println("Error. Nickname already in use. Please make sure no other instances are running and restart this application.");
				System.exit(3);
		}
		else
		{
			System.err.println("Internal event handler: Unknown internal event " + ev + ".");
		}
	}
	
	/** Sends a event to the bot, checking if it is a internal one,
	 * and if it is, it calls the appropriate method. Really just 
	 * a alias for {@link BotUtils#sendEvent(Event, EventArgs, IRCBase)}
	 * with <code>this</code> as the third argument :).
	 * @param ev The event to send.
	 * @param args The arguments to use.
	 */
	public void sendEvent(Event ev, EventArgs args)
	{
		BotUtils.sendEvent(ev, args, this);
	}
	
	/** Gets the list of {@link EventHandlerManager}s that are assigned {@link IRCEventListener}s
	 * that have subscribed to receive events from the bot.
	 * @return The list of {@link EventHandlerManager}s.
	 */
	List<EventHandlerManager> getListeners()
	{
		return _eventcoll.getListeners();
	}
	
	EventHandlerCollection getEventHandlerColl()
	{
		return _eventcoll;
	}
	
	private EventHandlerCollection _eventcoll;
	private List<Channel> _channels;
}
