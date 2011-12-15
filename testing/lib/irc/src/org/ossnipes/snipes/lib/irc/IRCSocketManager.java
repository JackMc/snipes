package org.ossnipes.snipes.lib.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.SocketFactory;

import org.ossnipes.snipes.lib.events.BotUtils;
import org.ossnipes.snipes.lib.events.ThreadLevel;

/* 
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
 * Manages the Socket to a IRC server. This class is meant to make sending and
 * receiving to/from a IRC server easier. It also allows the implementation to
 * be changed much easier.
 * 
 * @since Snipes 0.6
 * @author Jack McCracken
 */

public class IRCSocketManager implements InternalConstants, BotConstants, Runnable
{

	/**
	 * Creates a IRC Socket Manager object.
	 * 
	 * @param rawSocket
	 *            The Socket Object to manage. This Socket must be open or a
	 *            {@link IllegalArgumentException} will be thrown.
	 * @throws IOException
	 *             If there was a error (besides the stream being closed) while
	 *             opening the streams.
	 */
	public IRCSocketManager()
	{
		Runtime.getRuntime().addShutdownHook(new Thread(this));
		_options = BotOptions.getInst();
	}

	/**
	 * Sends a raw line to the IRC server.
	 * 
	 * @param line
	 *            The line to send to the server.
	 */
	public void sendRaw(String line)
	{
		if (!isConnected())
		{
			throw new NotConnectedException("You can't send something if you're not connected! Try a call to" +
			" IRCSocketManager.connect first!");
		}
		if (isVerbose())
		{
			System.out.println("US: " + line);
		}
		_writer.println(line);
	}

	/**
	 * Reads a line of text from the server.
	 * 
	 * @return The String from the server, null if a Exception occurs.
	 * @throws IOException
	 *             If we've been disconnected, or something else happens.
	 */
	public String recvRaw() throws IOException
	{
		try
		{
			return _reader.readLine();
		} catch (IOException e)
		{
			throw new IOException("We have been killed: " + e.getMessage(), e);
		}
	}

	public boolean isConnected()
	{
		if (_rawSocket == null)
		{
			return false;
		}
		else
		{
			return _rawSocket.isConnected();
		}
	}
	
	public void close()
	{
		try
		{
			_rawSocket.close();
		} catch (Exception e)
		{
			// We don't care.
		}
	}
	
	/** Sets the verbose value of the bot.
	 * @param on What the value should be set to.
	 */
	protected void setVerbose(boolean on)
	{
		_options.setVerbose(on);
	}

	/** Gets if the bot is verbose.
	 * @return If the verbose property is true.
	 */
	public boolean isVerbose()
	{
		return _options.isVerbose();
	}

	protected void debug(String line)
	{
		debug(line, Level.INFO);
	}

	protected void debug(String line, Level level)
	{
		if (_options.isDebugging())
			_logger.log(level, line);
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
		_options.setDebugging(on);
	}
	
	public boolean isDebugging()
	{
		return _options.isDebugging();
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
	public IRCSocketManager connect(InputHandler ih, String server, int port, String passwd, SocketFactory factory)
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

		// Quick, init the IRCReceiver before the server kills us for not
		// registering our USER, NICK and PING commands :P!
		_receiver = new IRCReceiver(this, ih);
		
		_rawSocket = _factory.createSocket(server, port);
		_reader = new BufferedReader(new InputStreamReader(_rawSocket.getInputStream()));
		_writer = new PrintStream(_rawSocket.getOutputStream());
		
		// Create/Start the recv Thread
		new Thread(_receiver, "Snipes-IRC-Framework-Receiver").start();

		// We can start!
		sendInit(passwd);
		// We're connected!
		return this;
	}

	
	public IRCSocketManager connect(InputHandler ih, String server, int port, SocketFactory factory)
	throws IOException, UnknownHostException
	{
		return connect(ih, server, port, null, factory);
	}
	
	/** Sends a few lines we need to the server before we start */
	private void sendInit(String passwd)
	{
		// PASS needs to be sent before the USER/NICK combination.
		if (passwd != null)
		{
			sendRaw("PASS :" + passwd);
		}
		
		sendRaw("USER " + (_user != null ? _user : DEFAULT_USER) + " 0 Snipes :" + (_realname != null ? _realname : DEFAULT_REALNAME));
		sendRaw("NICK " + (_nick != null ? _nick : DEFAULT_NICK));
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
	public IRCSocketManager connect(InputHandler ih, String server, int port) throws IOException,
	UnknownHostException
	{
		// Defaults to a SnipesSocketFactory if third parameter is null.
		return connect(ih, server, port, null);
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
	public IRCSocketManager connect(InputHandler ih, String server) throws IOException, UnknownHostException
	{
		// Connect to the server with the default IRC port and 
		// SocketFactory.
		return connect(ih, server, IRC_DEFAULT_PORT, null);
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

	/** Sets the bot's nick. If we are connected to a server, the server will be notified of
	 * the nick change.
	 * @param nick The nick to change to.
	 */
	public IRCSocketManager setNick(String nick)
	{
		if (nick == null)
		{
			throw new IllegalArgumentException("Nick cannot be null.");
		}
		this._nick = nick;
		// Before attempting a send operation, we have to make sure we're actually
		// connected to a server, and that the bot's connect() method has been called.
		if (!isConnected())
		{
			// Return back to the caller, we shouldn't try and set the nick on the server if
			// we're not connected to one.
			return this;
		}
		sendRaw("NICK " + _nick);
		return this;
	}
	
	/** Sets the bot's username (before the &#064; and after !). This should be called before connecting to a server, as there is
	 * no way in the protocol to change the username after the original USER command.
	 * @param name The username to set to.
	 * @return This instance, for chaining.
	 */
	public IRCSocketManager setUser(String newUser)
	{
		if (newUser == null)
		{
			throw new IllegalArgumentException("Name cannot be null.");
		}
		
		this._user = newUser;
		return this;
	}
	
	/** Joins a channel on the current IRC server.
	 * @param channel The channel to join.
	 * @return The current IRCSocketManager, for convenience.
	 */
	public IRCSocketManager join(String channel)
	{
		// If it's less than two (it doesn't have a prefix or is just the
		// prefix)
		if (channel.length() < 2)
		{
			throw new IllegalArgumentException("Channel name too short.");
		}
		
		// Get the first char of the channel
		char c = channel.charAt(0);

		// Check if they gave us a valid channel prefix
		if (!BotUtils.arrayContains(IRC_CHANPREFIXES, c))
		{
			throw new IllegalArgumentException("Channel prefix invalid.");
		}
		else
		{
			sendRaw("JOIN " + channel);
		}
		return this;
	}
	
	/** Sets the bot's realname. This should be called before connecting to a server, as there is
	 * no way in the protocol to change the realname after the original USER command.
	 * @param name The realname to set to.
	 * @return This instance, for chaining.
	 */
	public IRCSocketManager setRealname(String name)
	{
		if (name == null)
		{
			throw new IllegalArgumentException("Name cannot be null.");
		}
		
		this._realname = name;
		return this;
	}
	
	@Override
	public void run()
	{
		if (_rawSocket != null)
		{
			try {
					_rawSocket.close();
			} catch (IOException e) {
				// We're terminating. Silently ignore the error.
			}
		}
	}

	// Class-scope variables
	/** The current nick of the bot */
	private String _nick = DEFAULT_NICK;
	
	private BotOptions _options;
	/**
	 * The current username (see {@link BotConstants#DEFAULT_USER} for a
	 * definition of username)
	 */
	private String _user = DEFAULT_USER;

	private String _realname = DEFAULT_REALNAME;

	/** The SocketFactory which all connections to a server will be created with */
	private SocketFactory _factory;
	/**
	 * The IRCReciever that will be in a separate thread, passing messages to
	 * the handler
	 */
	private IRCReceiver _receiver;
	
	private static final Logger _logger = Logger.getLogger(IRCSocketManager.class.getCanonicalName());
	
	private Socket _rawSocket;
	
	private BufferedReader _reader;
	
	private PrintStream _writer;
}
