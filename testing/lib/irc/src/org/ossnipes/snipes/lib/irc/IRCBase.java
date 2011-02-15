package org.ossnipes.snipes.lib.irc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;

import javax.net.SocketFactory;

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
public abstract class IRCBase implements IRCConstants, BotConstants, 
IRCEventListener
{
	//TODO: Add more methods for doing things
	// Default constructor
	public IRCBase()
	{
		// Init topics and event manager lists and hashmaps.
		_topics = new HashMap<String,String>();
		_evmngrs = new ArrayList<EventHandlerManager>();
		addEventListener(this);
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
	public final Event[] register()
	{
		return new Event[] {};
	}
	
	public boolean isDebugging()
	{
		return BotOptions.DEBUG;
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
	public IRCBase connect(String server, int port, SocketFactory factory)
	throws IOException, UnknownHostException
	{
		Thread.currentThread().setName("Snipes-IRC-Framework-Main");
		// If port <= 0 || port > 65535, throw IllegalArgumentException
		// because port numbers can only be 1-65535 (The maximum value for a
		// 16-bit integer)
		if (port <= 0 || port > 65535)
		{
			throw new IllegalArgumentException(
			"Port must be between 1 and 65535.");
		}

		// If the server is null, throw a IllegalArgumentException (do I really
		// have to explain this?)
		if (server == null)
		{
			throw new IllegalArgumentException("Server may not be null");
		}

		// This used to be a series of ifs, but we can just turn it into a
		// conditional statement
		// Check if the factory is null, if it is, use SnipesSocketFactory's
		// default.
		_factory = (factory != null ? factory : SnipesSocketFactory
				.getDefault());

		// Create the socket, pass it to the new manager.
		_manager = new IRCSocketManager(_factory.createSocket(server, port));

		// Initialise the IRCInputHandler
		_handler = new IRCInputHandler(this);

		// Quick, init the IRCReceiver before the server kills us for not
		// registering our USER, NICK and PING commands :P!
		_receiver = new IRCReceiver(_manager, _handler);

		// Create/Start the recv Thread
		Thread t = new Thread(_receiver, "Snipes-IRC-Framework-Receiver");
		t.start();

		// We can start!
		sendInit();
		// We're connected!
		return this;
	}

	/** Sends a few lines we need to the server before we start */
	private void sendInit()
	{
		_manager.sendRaw("USER " + _user + " 0 Snipes :" + _realname);
		_manager.sendRaw("NICK " + _nick);
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
	public IRCBase connect(String server, int port) throws IOException,
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
	public IRCBase connect(String server) throws IOException, UnknownHostException
	{
		// Connect to the server with the default IRC port and 
		// SocketFactory.
		return connect(server, IRC_DEFAULT_PORT, null);
	}

	/** Sends a IRC PRIVMSG to the server.
	 * @param target The channel/nick to send to.
	 * @param msg The message to send.
	 */
	public void sendPrivMsg(String target, String msg)
	{
		sendRaw("PRIVMSG " + target + " :" + msg);
	}

	/** Sets the topic of the specified channel. If the channel is mode +t (topic protection)
	 * then the bot will be required to have operator status in channel.
	 * @param channel The channel to set the topic of.
	 * @param topic The topic to set it to.
	 */
	public void setTopic(String channel, String topic)
	{
		sendRaw("TOPIC " + channel + " " + topic);
	}

	/** Sends a PONG command to the server. A PONG command is the command to respond to a
	 * server's PING message (keeps us connected to the server.)
	 * @param server The server to specify in the PONG command.
	 */
	public void sendPong(String server)
	{
		sendRaw("PONG :" + server);
	}

	/** Sends a IRC WHO command, sending IRC_RESPONSE_CODE events for the different parts of
	 * the WHO response.
	 * @param name The name of the user, or the mask to use.
	 */
	public void who(String name)
	{
		sendRaw("WHO :" + name);
	}

	/** Parts (leaves) the specified channel.
	 * @param channel The channel to leave.
	 */
	public void part(String channel)
	{
		sendRaw("PART " + channel);
	}


	/** Kicks a user from a channel.
	 * @param user The nick of the user to kick. May be a comma-separated list (no spaces)
	 * @param channel The channel to kick from. If user was a comma separated list, this
	 * should also be one, specifying the channels to kick from.
	 * @param reason The reason to use for kicking. The protocol does not allow for specification
	 * of the reasons to be used for each user of a comma-separated list, so this will be treated
	 * as a single String. If this is null, a reason will not be given. (The server may fill it in
	 * for you with something like your nick.)
	 */
	public void kick(String user, String channel, String reason)
	{
		sendRaw("KICK " + channel + " " + user + (reason != null ? " :" + reason : ""));
	}

	/** Kicks a user from a channel. Calling this method is the same as kick(user,channel,null)
	 * @param user The nick of the user to kick. May be a comma-separated list (no spaces)
	 * @param channel The channel to kick from. If user was a comma separated list, this
	 * should also be one, specifying the channels to kick from.
	 */
	public void kick(String user, String channel)
	{
		kick(user, channel, null);
	}

	/** Sends a IRC NOTICE command to the server. NOTICEs are generally used for automatic
	 * responses. As such, NOTICEs are not counted in idle time.
	 * @param to The user or channel to send the NOTICE to.
	 * @param msg The message to send as the body of the command.
	 */
	public void sendNotice(String to, String msg)
	{
		sendRaw("NOTICE " + to + " :" + msg);
	}

	public void disconnect(String msg)
	{
		sendRaw("QUIT" + (msg != null ? " :" + msg : ""));
	}
	
	public void disconnect()
	{
		disconnect("Quit");
	}

	/**Sets a mode on a channel or user with the specified parameters.
	 * @param channel The channel to set the mode on. e.x.: "#Snipes"
	 * @param mode The mode to set. e.x.: "+qo".
	 * @param params The parameters to use. e.x.: "Unix Unix" (with mode +qo). May be null.
	 * If it is, no parameters are sent. Instead of providing null, try
	 * {@link #setMode(String, String)}.
	 */
	public void setMode(String channel, String mode, String params)
	{
		sendRaw("MODE " + channel + " " + mode + (params != null ? " " + params : ""));
	}

	public void setMode(String channel, String mode)
	{
		setMode(mode, channel, null);
	}

	/** Identifies to NickServ with the specified password.
	 * @param pass The password to use.
	 */
	public void nickServIdent(String pass)
	{
		// We try our best using a NICKSERV command.
		// We really don't have a way of checking the response, 
		// so we can't tell if it worked right or not.
		// Also, IRC services aren't standardized, so we would 
		// run into troubles with messages differing even if
		// the implementation allowed recieving of packets from
		// classes other than IRCInputHandler.
		sendRaw("NICKSERV IDENTIFY " + pass);
	}
	/** Sends a raw line to the server.
	 * @param line The line to send.
	 */
	public void sendRaw(String line)
	{
		_manager.sendRaw(line);
	}

	/** Sets the bot's nick. If we are connected to a server, the server will be notified of
	 * the nick change.
	 * @param nick The nick to change to.
	 */
	public IRCBase setNick(String nick)
	{
		if (nick == null)
		{
			throw new IllegalArgumentException("Nick cannot be null.");
		}
		this._nick = nick;
		// Before attempting a send operation, we have to make sure we're actually
		// connected to a server, and that the bot's connect() method has been called.
		if (_manager == null || !_manager.isConnected())
		{
			// Return back to the caller, we shouldn't try and set the nick on the server if
			// we're not connected to one.
			return this;
		}
		sendRaw("NICK " + _nick);
		return this;
	}
	/** Sets the bot's realname. This should be called before connecting to a server, as there is
	 * no way in the protocol to change the realname after the original USER command.
	 * @param name
	 * @return
	 */
	public IRCBase setRealname(String name)
	{
		if (name == null)
		{
			throw new IllegalArgumentException("Name cannot be null.");
		}
		
		this._realname = name;
		return this;
	}
	
	/** Joins a channel on the current IRC server.
	 * @param channel The channel to join.
	 * @return The current IRCBase, for convenience.
	 */
	public IRCBase join(String channel)
	{
		_manager.sendRaw("JOIN " + channel);
		who(channel);
		return this;
	}
	/** Used to handle a event sent by {@link #sendEvent(Event, EventArgs)}.
	 * @param ev The event that was sent.
	 * @param args The arguments for the event.
	 */
	public abstract void handleEvent(Event ev, EventArgs args);

	/** This method is called when a event in the {@link BotConstants#INT_EVENTS} array
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
		// When we first receive the topic from the server upon joining a channel or
		// it is changed.
		else if (ev == Event.IRC_JOIN_TOPIC || ev == Event.IRC_TOPIC)
		{
			_topics.put((String)args.getParam("channel"), (String)args.getParam("topic"));
		}
		else if (ev == Event.IRC_NICKINUSE)
		{
			System.err.println("Error. Nickname already in use. Please make sure no other instances are running and restart this application.");
			System.exit(3);
		}
		else
		{
			System.err.println("Internal event handler: Unknown internal event " + ev + ".");
		}
	}
	/** Gets the bot's current nick.
	 * @return The current nick.
	 */
	public String getNick()
	{
		return _nick;
	}

	/** Controls the printing of debug statements to the default Snipes logger.
	 * @param on If it should be turned on or off.
	 */
	public void setDebugging(boolean on)
	{
		BotOptions.DEBUG = on;
	}

	/** Adds a listener for events from the bot.
	 * @param listener The IRCEventListener
	 * @return The passed event listener, for convenience.
	 */
	public final IRCEventListener addEventListener(IRCEventListener listener)
	{
		if (listener == null)
		{
			throw new IllegalArgumentException("Cannot add null event handler.");
		}
		else if (_evmngrs.contains(listener))
		{
			throw new IllegalArgumentException("Event handler already added.");
		}
		else
		{
			debug("Added event listener: " + listener.getClass().getName() + 
					". This is #" + (_evmngrs.size() + 1) + ".");
			EventHandlerManager ehm = new EventHandlerManager(listener);
			for (Event e : listener.register())
			{
				ehm.addEvent(e);
			}
			_evmngrs.add(ehm);
			return listener;
		}
	}
	
	public final void removeEventListener(IRCEventListener listener)
	{
		if (listener == null)
		{
			throw new IllegalArgumentException("Cannot remove null event handler.");
		}
		else
		{
			List<EventHandlerManager> mans = this.getListeners();
			
			int i = 0;
			
			// Loop through the listeners
			while (i < mans.size())
			{
				EventHandlerManager ehm = mans.get(i);
				if (ehm.getManaged() == listener)
				{
					mans.remove(i);
				}
				i++;
			}
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

	/** Sets the verbose value of the bot.
	 * @param on What the value should be set to.
	 */
	protected void setVerbose(boolean on)
	{
		BotOptions.VERBOSE = on;
	}

	/** Gets if the bot is verbose.
	 * @return If the verbose property is true.
	 */
	protected boolean isVerbose()
	{
		return BotOptions.VERBOSE;
	}

	protected void debug(String line)
	{
		debug(line, Level.INFO);
	}

	protected void debug(String line, Level level)
	{
		if (BotOptions.DEBUG)
			_logger.log(level, line);
	}

	/** Gets the list of {@link EventHandlerManager}s that are assigned {@link IRCEventListener}s
	 * that have subscribed to receive events from the bot.
	 * @return The list of {@link EventHandlerManager}s.
	 */
	List<EventHandlerManager> getListeners()
	{
		return _evmngrs;
	}

	/** The current nick of the bot */
	private String _nick = DEFAULT_NICK;
	/**
	 * The current username (see {@link BotConstants#DEFAULT_USER} for a
	 * definition of username)
	 */
	private String _user = DEFAULT_USER;

	private String _realname = DEFAULT_REALNAME;

	/** The SocketFactory which all connections to a server will be created with */
	private SocketFactory _factory;
	/**
	 * The IRCSocketManager that will be used to manage the current server
	 * connection
	 */
	private IRCSocketManager _manager;
	/**
	 * The IRCReciever that will be in a separate thread, passing messages to
	 * the handler
	 */
	private IRCReceiver _receiver;
	/** The IRCInputHandler that will pass all received messages to us. */
	private IRCInputHandler _handler;

	/** Holds all the topics of the channels we're in. */
	private Map<String,String> _topics;

	private static final Logger _logger = Logger.getLogger(IRCBase.class.getCanonicalName());

	private List<EventHandlerManager> _evmngrs;
}
